// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.exception;

import java.util.stream.Collectors;

import cn.dinodev.spring.auth.exception.AuthorizationException;
import cn.dinodev.spring.auth.exception.NoPermissionException;
import cn.dinodev.spring.auth.exception.NotLoginException;
import cn.dinodev.spring.commons.exception.BusinessException;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.response.Status;
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
 * @author Cody Lu
 */

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

  /**
   * 处理业务异常
   * @param response HTTP响应对象
   * @param ex 业务异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(BusinessException.class)
  public Response<Object> businessExceptionHandler(HttpServletResponse response, BusinessException ex) {
    log.error("business exception occured: code-{}, msg-{}", ex.getCode(), ex.getMessage(), ex);
    var resp = Response.fail(Status.fail(ex.getCode(), ex.getMessage()));
    resp.setData(ex.getData());
    return resp;
  }

  /**
   * 处理空指针异常
   * @param response HTTP响应对象
   * @param ex 空指针异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(NullPointerException.class)
  public Response<Void> nullPointerExceptionHandler(HttpServletResponse response, NullPointerException ex) {
    log.error("NPE exception occured", ex);
    return Response.fail(Status.CODE.FAIL_EXCEPTION.withMsg("NPE"));
  }

  /**
   * 处理数据访问异常
   * @param response HTTP响应对象
   * @param ex 数据访问异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(DataAccessException.class)
  public Response<Void> dataAccessExceptionHandler(HttpServletResponse response, DataAccessException ex) {
    log.error("data access exception occured", ex);
    return Response.fail(Status.CODE.FAIL_QUERY_EXCEPTION);
  }

  /**
   * 处理用户未登录异常
   * @param request HTTP请求对象
   * @param ex 未登录异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(NotLoginException.class)
  public Response<Void> notLoginExceptionHandler(HttpServletRequest request, NotLoginException ex) {
    log.error("user not login exception on request {}", request.getRequestURL());
    return Response.fail(Status.CODE.FAIL_NOT_LOGIN);
  }

  /**
   * 处理无权限访问异常
   * @param request HTTP请求对象
   * @param ex 无权限异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(NoPermissionException.class)
  public Response<Void> noPermissionExceptionHandler(HttpServletRequest request, NoPermissionException ex) {
    log.error("user has no permission exception on request {}, {}", request.getRequestURL(), ex.getMessage());
    return Response.fail(Status.CODE.FAIL_NO_PERMISSION);
  }

  /**
   * 处理授权异常
   * @param request HTTP请求对象
   * @param ex 授权异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(AuthorizationException.class)
  public Response<Void> authExceptionHandler(HttpServletRequest request, AuthorizationException ex) {
    log.error("auth exception on request {}, {}", request.getRequestURL(), ex.getMessage());
    return Response.fail(Status.CODE.FAIL_AUTH);
  }

  /**
   * 处理非法参数异常
   * @param response HTTP响应对象
   * @param ex 非法参数异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public Response<Void> illegalArgumentExceptionHandler(HttpServletResponse response, IllegalArgumentException ex) {
    log.error("illegal argument exception occured", ex);
    return Response.fail(Status.CODE.FAIL_INVALID_PARAM);
  }

  /**
   * 处理约束验证异常
   * @param response HTTP响应对象
   * @param ex 约束验证异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public Response<Void> validateExceptionHandler(HttpServletResponse response, ConstraintViolationException ex) {
    log.error("validate exception occured", ex);
    var msg = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.joining("\n"));
    return Response.fail(Status.CODE.FAIL_VALIDATION.withMsg(msg));
  }

  /**
   * 处理方法参数验证异常
   * @param response HTTP响应对象
   * @param ex 方法参数验证异常
   * @return 包含错误信息的响应对象
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response<Void> methodArgumentNotValidExceptionHandler(HttpServletResponse response,
      MethodArgumentNotValidException ex) {
    log.error("validate exception occured", ex);
    var msg = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ":" + e.getDefaultMessage())
        .collect(Collectors.joining("\n"));
    return Response.fail(Status.CODE.FAIL_VALIDATION.withMsg(msg));
  }

}
