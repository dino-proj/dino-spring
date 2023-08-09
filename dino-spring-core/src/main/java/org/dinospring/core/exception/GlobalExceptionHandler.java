// Copyright 2021 dinodev.cn
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

import org.dinospring.auth.exception.AuthorizationException;
import org.dinospring.auth.exception.NoPermissionException;
import org.dinospring.auth.exception.NotLoginException;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public Response<Object> businessExceptionHandler(HttpServletResponse response, BusinessException ex) {
    log.error("business exception occured: code-{}, msg-{}", ex.getCode(), ex.getMessage(), ex);
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

  @ExceptionHandler(NotLoginException.class)
  public Response<Void> notLoginExceptionHandler(HttpServletRequest request, NotLoginException ex) {
    log.error("user not login exception on request {}", request.getRequestURL());
    return Response.fail(Status.CODE.FAIL_NOT_LOGIN);
  }

  @ExceptionHandler(NoPermissionException.class)
  public Response<Void> noPermissionExceptionHandler(HttpServletRequest request, NoPermissionException ex) {
    log.error("user has no permission exception on request {}, {}", request.getRequestURL(), ex.getMessage());
    return Response.fail(Status.CODE.FAIL_NO_PERMISSION);
  }

  @ExceptionHandler(AuthorizationException.class)
  public Response<Void> authExceptionHandler(HttpServletRequest request, AuthorizationException ex) {
    log.error("auth exception on request {}, {}", request.getRequestURL(), ex.getMessage());
    return Response.fail(Status.CODE.FAIL_AUTH);
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response<Void> methodArgumentNotValidExceptionHandler(HttpServletResponse response,
      MethodArgumentNotValidException ex) {
    log.error("validate exception occured", ex);
    var msg = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ":" + e.getDefaultMessage())
        .collect(Collectors.joining("\n"));
    return Response.fail(Status.CODE.FAIL_VALIDATION.withMsg(msg));
  }

}
