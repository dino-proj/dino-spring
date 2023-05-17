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

package org.dinospring.auth.session;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Pair;
import org.dinospring.auth.DinoAuth;
import org.dinospring.auth.exception.NotLoginException;
import org.springframework.http.server.PathContainer;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.extern.slf4j.Slf4j;

/**
 * 缺省的会话打开过滤器，用于在每次请求时打开Auth会话
 *
 * @author tuuboo
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
