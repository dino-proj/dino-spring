// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.auth.session;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.dinospring.auth.DinoAuth;
import org.dinospring.auth.exception.NotLoginException;
import org.springframework.http.server.PathContainer;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 缺省的会话打开过滤器，用于在每次请求时打开Auth会话
 *
 * @author Cody Lu
 * @date 2022-04-11 17:38:45
 */

@Slf4j
public class DefaultAuthSessionOpenFilter extends OncePerRequestFilter {

  private final List<AuthSessionResolver<? extends AuthSession>> authSessionResolvers;

  private List<PathPattern> whiteListPattern = List.of();

  public DefaultAuthSessionOpenFilter(Collection<AuthSessionResolver<? extends AuthSession>> authSessionResolvers) {
    this.authSessionResolvers = List.copyOf(authSessionResolvers);
    log.info("{} authSessionResolvers added: {}", authSessionResolvers.size(), authSessionResolvers);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (isWhiteList(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    var authSession = resolveSession(request);
    if (Objects.isNull(authSession)) {
      throw new NotLoginException();
    }

    DinoAuth.setAuthSession(authSession.getRight());
    try {
      filterChain.doFilter(request, response);
    } finally {
      DinoAuth.setAuthSession(null);
      authSession.getLeft().closeSession(request, authSession.getRight());
    }

  }

  @SuppressWarnings("squid:S1452")
  protected Pair<AuthSessionResolver<? extends AuthSession>, AuthSession> resolveSession(HttpServletRequest request) {
    for (var authSessionResolver : authSessionResolvers) {
      var authSession = authSessionResolver.resolveSession(request);
      if (Objects.nonNull(authSession)) {
        return Pair.of(authSessionResolver, authSession);
      }
    }
    return null;
  }

  /**
   * 设置白名单URL，如果请求的URL匹配白名单，则不打开AuthSession
   * <p>白名单路径规则，参见{@link }</p>
   * @param whitelist
   */
  public void setWhitelist(List<String> whitelist) {
    this.whiteListPattern = whitelist.stream().map(PathPatternParser.defaultInstance::parse).filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private boolean isWhiteList(HttpServletRequest request) {
    if (whiteListPattern.isEmpty()) {
      return false;
    }

    var requestPath = PathContainer.parsePath(request.getServletPath());
    return whiteListPattern.stream().anyMatch(pattern -> pattern.matches(requestPath));
  }

}
