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

package org.dinospring.auth.session;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>AuthSession Resolver From {@link HttpServletRequest}</P>
 * <p>配合{@link DefaultAuthSessionOpenFilter}使用</p>
 *
 *
 * @author tuuboo
 * @date 2022-04-09 21:50:24
 */

public interface AuthSessionHttpResolver<S extends AuthSession> {

  /**
   * <p>从{@link HttpServletRequest}中获取{@link AuthSession}</p>
   *
   * @param request http请求
   * @return {@link AuthSession}，如果不存在，返回null
   */
  S resolveSession(HttpServletRequest request);

  /**
   * <p>关闭{@link AuthSession}</p>
   * @param request http请求
   * @param sessionId 会话ID，如果不存在，则为null
   */
  void closeSession(HttpServletRequest request, String sessionId);
}