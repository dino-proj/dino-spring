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

import java.util.Base64;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.core.sys.login.config.LoginModuleProperties;
import org.dinospring.core.sys.token.Token;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
public class ShiroAuthFilter extends AuthenticatingFilter {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private LoginModuleProperties loginModuleProperties;

  /**
   * 判断用户是否想要登入。
   * 检测header里面是否包含Authorization字段即可
   */
  @Override
  protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
    if (super.isLoginRequest(request, response)) {
      return false;
    }
    var req = WebUtils.toHttp(request);
    String authorization = req.getHeader("Authorization");
    return authorization != null;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    var req = WebUtils.toHttp(request);
    if (log.isDebugEnabled()) {
      log.debug("Access denied uri={}", req.getRequestURI());
    }
    var resp = WebUtils.toHttp(response);
    resp.setContentType("application/json;charset=UTF-8");
    objectMapper.writeValue(resp.getWriter(), Response.fail(Status.CODE.FAIL_NO_PERMISSION));

    return false;
  }

  @Override
  protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
    var req = WebUtils.toHttp(request);
    String authorization = req.getHeader(loginModuleProperties.getToken().getHttpHeaderName());
    var prinStr = Token.extractPrinc(authorization);
    var tokenStr = Token.extractToken(authorization);

    if (StringUtils.isBlank(authorization)) {
      return null;
    }
    var princ = objectMapper.readValue(Base64.getUrlDecoder().decode(prinStr), TokenPrincaple.class);
    return ShiroAuthToken.of(princ, tokenStr);
  }

}
