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

package org.dinospring.core.sys.audit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.botbrain.dino.utils.IPUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.dinospring.commons.context.DinoContext;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.AsyncWorker;
import org.dinospring.core.annotion.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于记录日志的Aspect,
 * 请使用 {@link AuditLogEntity} 注解
 *
 * @author tuuboo
 */
@Aspect
@Component
@Slf4j
public class AuditLogAspect {
  @Autowired
  private AsyncWorker asyncWorker;

  @Autowired
  private AuditLogService auditLogService;

  @Autowired
  private DinoContext dinoAppContext;

  private static int maxLength = 1000;

  /**
   * 注解切面
   */
  @Pointcut("@annotation(org.dinospring.core.annotion.AuditLog)")
  public void pointCut() {
    // Do Nothing
  }

  /**
   * 处理函数返回
   * @param joinPoint
   * @param returnObj
   */
  @AfterReturning(value = "pointCut()", returning = "returnObj")
  public void afterReturningHandler(JoinPoint joinPoint, Object returnObj) {
    AuditLogEntity auditLog = buildAuditLog(joinPoint);
    if (auditLog == null) {
      return;
    }
    // 处理返回结果
    int code = 0;
    String errorMsg = null;
    if (returnObj instanceof Response) {
      var resp = (Response<?>) returnObj;
      code = resp.getCode();
      if (code > 0) {
        errorMsg = resp.getMsg();
      }
    }
    auditLog.setStatusCode(code).setErrorMsg(errorMsg);
    // 异步保存操作日志
    asyncWorker.exec(() -> auditLogService.save(auditLog));
  }

  /**
   * 处理异常
   * @param joinPoint
   * @param throwable
   */
  @AfterThrowing(value = "pointCut()", throwing = "throwable")
  public void afterThrowingHandler(JoinPoint joinPoint, Throwable throwable) {
    AuditLogEntity auditLog = buildAuditLog(joinPoint);
    if (auditLog == null) {
      return;
    }
    // 处理返回结果
    int code = Status.CODE.FAIL_EXCEPTION.getCode();
    String errorMsg = null;
    if (throwable != null) {
      if (throwable instanceof BusinessException) {
        BusinessException be = (BusinessException) throwable;
        code = be.getCode();
      }
      errorMsg = throwable.toString();
      StackTraceElement[] stackTraceElements = throwable.getStackTrace();
      if (ArrayUtils.isNotEmpty(stackTraceElements)) {
        errorMsg += "CAUSE: " + stackTraceElements[0].toString();
      }
    }
    auditLog.setStatusCode(code).setErrorMsg(StringUtils.left(errorMsg, maxLength));
    // 异步保存操作日志
    asyncWorker.exec(() -> auditLogService.save(auditLog));
  }

  /**
   * 构建操作日志对象
   * @param joinPoint
   * @return
   */
  private AuditLogEntity buildAuditLog(JoinPoint joinPoint) {
    AuditLogEntity auditLog = new AuditLogEntity();
    // 当前请求信息
    ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (ra == null) {
      log.error("cannot find RequestAttributes");
      return null;
    }
    HttpServletRequest request = ra.getRequest();
    auditLog.setRequestMethod(request.getMethod()).setRequestUri(request.getRequestURI())
        .setRequestIp(IPUtils.getIpAddr(request));

    // 请求参数
    Map<String, String> params = new HashMap<>(16);
    request.getParameterMap().entrySet().stream()
        .forEach(kv -> params.put(kv.getKey(), StringUtils.join(kv.getValue(), ',')));
    String paramsJson = JSON.toJSONString(params);
    paramsJson = StringUtils.left(paramsJson, maxLength);
    auditLog.setRequestParams(paramsJson);

    // 操作用户信息
    var currentUser = dinoAppContext.currentUser();
    if (currentUser != null) {
      auditLog.setUserType(currentUser.getUserType().toString()).setUserId(String.valueOf(currentUser.getId()))
          .setUserDisplayname(currentUser.getDisplayName()).setTenantId(currentUser.getTenantId());
    }

    // 补充注解信息
    Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
    AuditLog auditLogAnno = AnnotationUtils.getAnnotation(method, AuditLog.class);
    Assert.notNull(auditLogAnno, "impossible null");
    String businessObj = auditLogAnno.business();
    if (StringUtils.isEmpty(businessObj)) {
      Class<?> entityClazz = getBusinessClass(method.getDeclaringClass());
      if (entityClazz != null) {
        businessObj = entityClazz.getSimpleName();
      } else {
        log.warn("@auditLog(operation='{}') 注解未识别到class泛型参数，请指定 businessObj", auditLogAnno.op());
      }
    }
    auditLog.setBusiness(businessObj).setOperation(auditLogAnno.op());

    return auditLog;
  }

  /**
   * 获取类的第一个泛型参数，作为其业务类
   * @param hostClass
   * @return
   */
  private static Class<?> getBusinessClass(Class<?> hostClass) {

    ResolvableType resolvableType = ResolvableType.forClass(hostClass).getSuperType();
    ResolvableType[] types = resolvableType.getGenerics();
    if (types.length == 0) {
      types = resolvableType.getSuperType().getGenerics();
    }
    if (types.length > 0) {
      return types[0].resolve();
    }
    return null;
  }

}
