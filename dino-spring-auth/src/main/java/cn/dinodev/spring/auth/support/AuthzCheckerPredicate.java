// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.support;

import java.util.Objects;
import java.util.function.Predicate;

import org.aopalliance.intercept.MethodInvocation;
import cn.dinodev.spring.auth.AuthzChecker;
import cn.dinodev.spring.auth.exception.NoPermissionException;
import cn.dinodev.spring.auth.exception.NotLoginException;
import cn.dinodev.spring.auth.session.AuthSession;

/**
 * 权限检查适配器，转换为{@link Predicate}
 *
 * @author Cody Lu
 * @date 2022-04-07 02:40:17
 */

public class AuthzCheckerPredicate implements AuthzChecker {
  public static final String SECONDARY_AUTHZ_KEY = "SECONDARY_AUTHZ";

  private final Predicate<AuthSession> predicate;

  public AuthzCheckerPredicate(Predicate<AuthSession> predicate) {
    this.predicate = predicate;
  }

  @Override
  public void assertPermmited(AuthSession authSession, MethodInvocation invocation) {
    if (Objects.isNull(authSession)) {
      throw new NotLoginException();
    }
    if (!predicate.test(authSession)) {
      throw new NoPermissionException();
    }
  }

  @Override
  public boolean isPermmited(AuthSession session, MethodInvocation mi) {
    return predicate.test(session);
  }

}
