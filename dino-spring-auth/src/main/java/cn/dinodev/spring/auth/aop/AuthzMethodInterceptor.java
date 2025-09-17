// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.aop;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import cn.dinodev.spring.auth.AuthzChecker;
import cn.dinodev.spring.auth.DinoAuth;
import cn.dinodev.spring.auth.exception.AuthorizationException;
import cn.dinodev.spring.auth.session.AuthSession;
import cn.dinodev.spring.auth.support.AuthzCheckerCustomBean;
import cn.dinodev.spring.auth.support.AuthzCheckerLoginAs;
import cn.dinodev.spring.auth.support.AuthzCheckerPermission;
import cn.dinodev.spring.auth.support.AuthzCheckerRole;
import org.springframework.beans.factory.BeanFactory;

/**
 * 权限检查拦截器
 * @author Cody Lu
 * @date 2022-04-09 19:30:52
 */

public class AuthzMethodInterceptor implements MethodInterceptor {

  private Collection<AuthzChecker> checkers;
  private Supplier<AuthSession> sessionSupplier;

  public AuthzMethodInterceptor(BeanFactory beanFactory) {
    this(DinoAuth::getAuthSession, beanFactory);
  }

  public AuthzMethodInterceptor(Supplier<AuthSession> sessionSupplier, BeanFactory beanFactory) {
    this.sessionSupplier = sessionSupplier;
    this.checkers = Arrays.asList(// chekers
        new AuthzCheckerPermission(), // check Permission
        new AuthzCheckerRole(), // check Role
        new AuthzCheckerLoginAs(), // check LoginAs
        new AuthzCheckerCustomBean(beanFactory) // check CustomBean
    );
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      for (var checker : this.checkers) {
        if (checker.support(invocation)) {
          checker.assertPermmited(this.sessionSupplier.get(), invocation);
        }
      }
    } catch (AuthorizationException e) {
      if (Objects.isNull(e.getCause())) {
        // tell the user which method they attempted to invoke
        e.initCause(new AuthorizationException(
            "Not authorized to invoke method: " + invocation.getMethod().getName() + "(...) @ "
                + invocation.getThis().getClass()));
      }
      throw e;
    } catch (RuntimeException e) {
      if (Objects.isNull(e.getCause())) {
        // tell the user which method they attempted to invoke
        e.initCause(
            new Throwable("Exception occured when invoke method: " + invocation.getMethod().getName() + "(...) @ "
                + invocation.getThis().getClass()));
      }
      throw e;
    }
    return invocation.proceed();
  }

}
