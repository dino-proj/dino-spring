package org.dinospring.core.exception;

import javax.servlet.http.HttpServletResponse;

import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public Response<Object> businessExceptionHandler(HttpServletResponse response, BusinessException ex) {
    log.warn("business exception occured: code-{}, msg-{}", ex.getCode(), ex.getMessage(), ex);
    var resp = Response.fail(Status.fail(ex.getCode(), ex.getMessage()));
    resp.setData(ex.getData());
    return resp;
  }

  @ExceptionHandler(NullPointerException.class)
  public Response<String> nullPointerExceptionHandler(HttpServletResponse response, NullPointerException ex) {
    log.error("NPE exception occured", ex);
    return Response.fail(Status.CODE.FAIL_EXCEPTION);
  }

  @ExceptionHandler(DataAccessException.class)
  public Response<String> dataAccessExceptionHandler(HttpServletResponse response, DataAccessException ex) {
    log.error("data access exception occured", ex);
    return Response.fail(Status.CODE.FAIL_QUERY_EXCEPTION);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public Response<String> illegalArgumentExceptionHandler(HttpServletResponse response, IllegalArgumentException ex) {
    log.error("illegal argument exception occured", ex);
    return Response.fail(Status.CODE.FAIL_INVALID_PARAM);
  }

}
