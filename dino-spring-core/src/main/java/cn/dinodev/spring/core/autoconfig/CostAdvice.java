// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.autoconfig;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import cn.dinodev.spring.commons.exception.BusinessException;
import cn.dinodev.spring.commons.response.Response;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
@Component
@Aspect
public class CostAdvice {

  @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)"
      + " || @annotation(org.springframework.web.bind.annotation.GetMapping)"
      + " || @annotation(org.springframework.web.bind.annotation.PostMapping)"
      + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
      + " || @annotation(org.springframework.web.bind.annotation.PutMapping)")
  public void costPointcut() {
    //do nothing
  }

  @Around("costPointcut()")
  public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      if (log.isDebugEnabled()) {
        log.debug("around:{}", joinPoint.toLongString());
      }
      Object result = joinPoint.proceed();
      if (result == null || !Response.class.isAssignableFrom(result.getClass())) {
        return result;
      }
      Response<?> resp = (Response<?>) result;
      long end = System.currentTimeMillis();
      long cost = end - start;
      if (log.isDebugEnabled()) {
        log.debug("around {} Use time: {}ms!", joinPoint, cost);
      }
      if (0 != resp.getCode()) {
        log.error("around {} error.msg :{}", joinPoint, resp.getMsg());
      }
      resp.setCost(cost);
      return result;

    } catch (Throwable e) {
      if (!BusinessException.class.isAssignableFrom(e.getClass())) {
        log.error("around {} Use time: {}ms with exception ", joinPoint, System.currentTimeMillis() - start, e);
      }
      throw e;
    }

  }
}
