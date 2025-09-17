// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 将filter的异常重新抛出，让其被全局异常处理器捕获
 * @author Cody Lu
 * @date 2022-05-06 19:46:56
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorRethrowController implements ErrorController {

  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public void handleError(HttpServletRequest request, HttpServletResponse response) throws Throwable {
    if (request.getAttribute("jakarta.servlet.error.exception") != null) {
      response.setStatus(HttpStatus.OK.value());
      throw (Throwable) request.getAttribute("jakarta.servlet.error.exception");
    }
  }
}
