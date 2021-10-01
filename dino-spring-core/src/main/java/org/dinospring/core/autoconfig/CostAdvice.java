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
