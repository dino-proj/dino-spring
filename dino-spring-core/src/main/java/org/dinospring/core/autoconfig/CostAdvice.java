package org.dinospring.core.autoconfig;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Response;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

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
      log.info("around:{}", joinPoint.toLongString());
      Object result = joinPoint.proceed();
      if (result == null || !Response.class.isAssignableFrom(result.getClass())) {
        return result;
      }
      Response<?> resp = (Response<?>) result;
      long end = System.currentTimeMillis();
      long cost = end - start;
      log.info("around {} Use time: {}ms!", joinPoint, cost);
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
