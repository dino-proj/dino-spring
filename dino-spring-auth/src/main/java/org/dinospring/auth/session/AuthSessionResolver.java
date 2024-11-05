// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.auth.session;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>AuthSession Resolver From {@link HttpServletRequest}</P>
 * <p>配合{@link DefaultAuthSessionOpenFilter}使用</p>
 *
 *
 * @author Cody LU
 * @date 2022-04-09 21:50:24
 */

public interface AuthSessionResolver<S extends AuthSession> {

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
   * @param session 会话，如果不存在，则为null
   */
  void closeSession(HttpServletRequest request, Object session);
}