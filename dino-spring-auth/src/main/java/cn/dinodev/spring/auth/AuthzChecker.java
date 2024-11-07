// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth;

import org.aopalliance.intercept.MethodInvocation;
import cn.dinodev.spring.auth.exception.AuthorizationException;
import cn.dinodev.spring.auth.session.AuthSession;

/**
 * 权限检查接口
 * @author Cody Lu
 * @date 2022-04-07 01:08:14
 */
public interface AuthzChecker {
  /**
   * 权限断言，如果权限不足，则抛出异常
   * @param session 登录会话
   * @param mi 方法调用
   * @throws AuthorizationException 权限不足时，抛出异常
   */
  default void assertPermmited(AuthSession session, MethodInvocation mi) throws AuthorizationException {
    if (!isPermmited(session, mi)) {
      throw new AuthorizationException("No permission to access " + mi.getMethod().getName());
    }
  }

  /**
   * 是否支持该调用的权限检测
   *
   * @param mi 方法调用
   * @return
   */
  default boolean support(MethodInvocation mi) {
    return true;
  }

  /**
   * 检查权限，如果权限不足，则返回false
   * @param session 登录会话
   * @param mi  方法调用
   * @return true:权限足够，false:权限不足
   */
  boolean isPermmited(AuthSession session, MethodInvocation mi);
}
