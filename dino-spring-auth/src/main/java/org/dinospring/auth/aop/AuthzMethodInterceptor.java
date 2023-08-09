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

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.dinospring.auth.AuthzChecker;
import org.dinospring.auth.DinoAuth;
import org.dinospring.auth.exception.AuthorizationException;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.auth.support.AuthzCheckerLogin;
import org.dinospring.auth.support.AuthzCheckerPermission;
import org.dinospring.auth.support.AuthzCheckerRole;

/**
 * 权限检查拦截器
 * @author Cody LU
 * @date 2022-04-09 19:30:52
 */

public class AuthzMethodInterceptor implements MethodInterceptor {

  private Collection<AuthzChecker> checkers;
  private Supplier<AuthSession> sessionSupplier;

  public AuthzMethodInterceptor() {
    this(DinoAuth::getAuthSession);
  }

  public AuthzMethodInterceptor(Supplier<AuthSession> sessionSupplier) {
    this.sessionSupplier = sessionSupplier;
    this.checkers = Arrays.asList(new AuthzCheckerPermission(), new AuthzCheckerRole(), new AuthzCheckerLogin());
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      for (var checker : checkers) {
        if (checker.support(invocation)) {
          checker.assertPermmited(sessionSupplier.get(), invocation);
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
