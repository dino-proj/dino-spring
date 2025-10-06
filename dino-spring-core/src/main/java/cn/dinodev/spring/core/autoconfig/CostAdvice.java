// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.autoconfig;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cn.dinodev.spring.commons.exception.BusinessException;
import cn.dinodev.spring.commons.response.Response;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
@Component
@Aspect
public class CostAdvice {

  /**
   * 定义切入点，拦截所有Controller的请求映射方法
   */
  @Pointcut("""
      @annotation(org.springframework.web.bind.annotation.RequestMapping)
      || @annotation(org.springframework.web.bind.annotation.GetMapping)
      || @annotation(org.springframework.web.bind.annotation.PostMapping)
      || @annotation(org.springframework.web.bind.annotation.DeleteMapping)
      || @annotation(org.springframework.web.bind.annotation.PutMapping)
      """)
  public void costPointcut() {
    //do nothing
  }

  /**
   * 环绕通知，用于统计请求执行时间
   * @param joinPoint 连接点
   * @return 方法执行结果
   * @throws Throwable 方法执行异常
   */
  @Around("costPointcut()")
  @SuppressWarnings("PMD.AvoidRethrowingException")
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

    } catch (BusinessException e) {
      // 业务异常不记录日志，直接抛出
      throw e;
    } catch (Exception e) {
      // 非业务异常记录日志后抛出
      if (log.isErrorEnabled()) {
        log.error("around {} Use time: {}ms with exception ", joinPoint, System.currentTimeMillis() - start, e);
      }
      throw e;
    }

  }
}
