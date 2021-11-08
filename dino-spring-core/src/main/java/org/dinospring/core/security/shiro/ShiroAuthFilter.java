// Copyright 2021 dinospring.cn
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

package org.dinospring.core.security.shiro;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.core.sys.token.Token;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
public class ShiroAuthFilter extends BearerHttpAuthenticationFilter {

  @Autowired
  private ObjectMapper objectMapper;

  private static final String DINO_AUTH = "DinoAuth";

  @Getter
  private final String authHeader;

  @Getter
  @Setter
  private List<String> whiteList;

  public ShiroAuthFilter(String authHeader) {
    super();
    this.authHeader = authHeader;
    setAuthcScheme(DINO_AUTH);
    setAuthzScheme(DINO_AUTH);
  }

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    var path = WebUtils.toHttp(request).getServletPath();

    if (CollectionUtils.isNotEmpty(whiteList)) {
      for (var uri : whiteList) {
        if (pathMatcher.matches(uri, path)) {
          return true;
        }
      }
    }

    return super.isAccessAllowed(request, response, mappedValue);
  }

  @Override
  protected boolean isLoginAttempt(String authzHeader) {
    return StringUtils.isNotBlank(authzHeader);
  }

  @Override
  protected String getAuthzHeader(ServletRequest request) {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);
    return httpRequest.getHeader(authHeader);
  }

  @Override
  protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
    var req = WebUtils.toHttp(request);
    if (log.isDebugEnabled()) {
      log.debug("Access denied uri={}", req.getRequestURI());
    }

    var resp = WebUtils.toHttp(response);
    resp.setStatus(HttpStatus.OK.value());
    resp.setContentType("application/json;charset=UTF-8");
    resp.setHeader("Access-Control-Allow-Origin", "*");
    try (var out = resp.getOutputStream()) {
      var errors = objectMapper.writeValueAsBytes(Response.fail(Status.CODE.FAIL_INVALID_AUTH_TOKEN));
      out.write(errors);
    } catch (IOException e) {
      log.error("error occured while sendChallenge", e);
    }

    return false;
  }

  @Override
  protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
    String authzHeader = getAuthzHeader(request);

    if (StringUtils.isBlank(authzHeader)) {
      return null;
    }

    var prinStr = Token.extractPrinc(authzHeader);
    var tokenStr = Token.extractToken(authzHeader);

    try {
      var princ = objectMapper.readValue(Base64.getUrlDecoder().decode(prinStr), TokenPrincaple.class);
      return ShiroAuthToken.of(princ, tokenStr);
    } catch (IOException e) {
      log.error("error occured while createToken from[{}]", prinStr, e);
      return null;
    }
  }

}
