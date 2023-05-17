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
package org.dinospring.auth;

import java.util.Objects;

import org.dinospring.auth.session.AuthSession;
import org.dinospring.commons.utils.InheritableThreadLocalMap;

import lombok.experimental.UtilityClass;

/**
 * 权限相关的工具类
 * @author tuuboo
 * @date 2022-04-08 17:32:22
 */
@UtilityClass
public class DinoAuth {

  private static final InheritableThreadLocalMap RESOURCES = new InheritableThreadLocalMap();
  private static final String AUTH_SESSION_KEY = "AUTH_SESSION";

  /**
   * 获取登录会话
   * @return 登录会话,如果未登录则返回null
   */
  public static <T extends AuthSession> T getAuthSession() {
    return RESOURCES.get(AUTH_SESSION_KEY);
  }

  /**
   * 设置登录会话
   * @param authSession
   */
  public static <T extends AuthSession> void setAuthSession(T authSession) {
    RESOURCES.put(AUTH_SESSION_KEY, authSession);
  }

  public static boolean isLogin() {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.isLogin();
    }
    return false;
  }
}
