// Copyright 2022 dinodev.cn
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

package org.dinospring.auth.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.beanutils.MethodUtils;
import org.dinospring.auth.annotation.CheckAuthz;
import org.dinospring.auth.annotation.CheckLoginAs;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.auth.annotation.CheckResource;
import org.dinospring.auth.annotation.CheckRole;
import org.dinospring.auth.session.AuthSession;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * 基于注解的权限检查的切面，将检查权限的注解转换为检查权限的切点
 * @author Cody LU
 * @date 2022-04-07 21:26:39
 */

public class AuthzAnnotationPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

  protected static final List<Class<? extends Annotation>> AUTHZ_ANNOTATION_CLASSES = Arrays.asList(
      CheckPermission.class, CheckRole.class, CheckLoginAs.class, CheckResource.class, CheckAuthz.class);

  private transient MethodMatcher methodMatcher;

  public AuthzAnnotationPointcutAdvisor() {
    this.setAdvice(new AuthzMethodInterceptor());
  }

  public AuthzAnnotationPointcutAdvisor(Supplier<AuthSession> sessionSupplier) {
    this.setAdvice(new AuthzMethodInterceptor(sessionSupplier));
  }

  public AuthzAnnotationPointcutAdvisor(MethodMatcher methodMatcher) {
    this.methodMatcher = methodMatcher;
    this.setAdvice(new AuthzMethodInterceptor());
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    if (isAuthzAnnotationPresent(method)) {
      return secondaryMatch(method, targetClass);
    }
    if (Objects.nonNull(targetClass)) {
      if (isAuthzAnnotationPresent(targetClass)) {
        return secondaryMatch(method, targetClass);
      }
      // check the implement method of the target class
      var m = MethodUtils.getAccessibleMethod(targetClass, method.getName(), method.getParameterTypes());
      if (isAuthzAnnotationPresent(m)) {
        return secondaryMatch(method, targetClass);
      }
    }
    return false;
  }

  protected boolean isAuthzAnnotationPresent(Class<?> clss) {
    for (Class<? extends Annotation> annoClss : AUTHZ_ANNOTATION_CLASSES) {
      if (AnnotatedElementUtils.hasAnnotation(clss, annoClss)) {
        return true;
      }
    }
    return false;
  }

  protected boolean isAuthzAnnotationPresent(Method method) {
    if (Objects.isNull(method)) {
      return false;
    }
    for (Class<? extends Annotation> annoClss : AUTHZ_ANNOTATION_CLASSES) {
      if (AnnotatedElementUtils.hasAnnotation(method, annoClss)) {
        return true;
      }
    }
    return false;
  }

  protected boolean secondaryMatch(Method method, Class<?> targetClass) {
    if (Objects.nonNull(methodMatcher)) {
      return methodMatcher.matches(method, targetClass);
    }
    return true;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 10000;
  }
}
