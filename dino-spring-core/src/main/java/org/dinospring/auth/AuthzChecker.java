// Copyright 2022 dinospring.cn
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

package org.dinospring.auth;

import org.aopalliance.intercept.MethodInvocation;
import org.dinospring.auth.exception.AuthorizationException;
import org.dinospring.auth.session.AuthSession;

/**
 * 权限检查接口
 * @author tuuboo
 * @date 2022-04-07 01:08:14
 */
public interface AuthzChecker {
  /**
   * 权限断言，如果权限不足，则抛出异常
   * @param session 登录会话
   * @param invocation 方法调用
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
