// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public Response<Object> businessExceptionHandler(HttpServletResponse response, BusinessException ex) {
    log.warn("business exception occured: code-{}, msg-{}", ex.getCode(), ex.getMessage(), ex);
    var resp = Response.fail(Status.fail(ex.getCode(), ex.getMessage()));
    resp.setData(ex.getData());
    return resp;
  }

  @ExceptionHandler(NullPointerException.class)
  public Response<Void> nullPointerExceptionHandler(HttpServletResponse response, NullPointerException ex) {
    log.error("NPE exception occured", ex);
    return Response.fail(Status.CODE.FAIL_EXCEPTION.withMsg("NPE"));
  }

  @ExceptionHandler(DataAccessException.class)
  public Response<Void> dataAccessExceptionHandler(HttpServletResponse response, DataAccessException ex) {
    log.error("data access exception occured", ex);
    return Response.fail(Status.CODE.FAIL_QUERY_EXCEPTION);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public Response<Void> illegalArgumentExceptionHandler(HttpServletResponse response, IllegalArgumentException ex) {
    log.error("illegal argument exception occured", ex);
    return Response.fail(Status.CODE.FAIL_INVALID_PARAM);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public Response<Void> validateExceptionHandler(HttpServletResponse response, ConstraintViolationException ex) {
    log.error("validate exception occured", ex);
    var msg = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.joining("\n"));
    return Response.fail(Status.CODE.FAIL_VALIDATION.withMsg(msg));
  }

}
