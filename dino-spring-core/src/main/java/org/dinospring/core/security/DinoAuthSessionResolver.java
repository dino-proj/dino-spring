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

package org.dinospring.core.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.Permission;
import org.dinospring.auth.session.AuthInfoProvider;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.auth.session.AuthSessionResolver;
import org.dinospring.commons.context.DinoContext;
import org.dinospring.commons.function.Suppliers;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.security.DinoAuthSessionResolver.DinoAuthSession;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.core.sys.token.Token;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 * @date 2022-04-10 18:56:55
 */

@Slf4j
public class DinoAuthSessionResolver implements AuthSessionResolver<DinoAuthSession> {

  @Autowired
  TokenService tokenService;

  @Autowired
  private TenantService tenantService;

  @Autowired
  private UserServiceProvider userServiceProvider;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DinoContext context;

  @Autowired
  private AuthInfoProvider<User<?>> authzInfoProvider;

  @Getter
  private final String authHeader;

  public DinoAuthSessionResolver(String authHeader) {
    this.authHeader = authHeader;
  }

  @Override
  public DinoAuthSession resolveSession(HttpServletRequest request) {
    String authzHeader = request.getHeader(getAuthHeader());

    if (StringUtils.isBlank(authzHeader)) {
      return null;
    }

    var prinStr = Token.extractPrinc(authzHeader);
    var tokenStr = Token.extractToken(authzHeader);

    try {
      var princ = objectMapper.readValue(Base64.getUrlDecoder().decode(prinStr), TokenPrincaple.class);
      log.info("req http:{}",request.getRequestURL());
      // ??????token????????????
      Assert.isTrue(tokenService.checkLoginToken(princ, tokenStr), Status.CODE.FAIL_INVALID_AUTH_TOKEN);

      // ????????????Service
      var userType = userServiceProvider.resolveUserType(princ.getUserType());
      Assert.notNull(userType, "user type not found");
      var userService = userServiceProvider.resolveUserService(userType);
      // ??????????????????
      var user = userService.getUserById(userType, princ.getUserId()).orElse(null);
      Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

      context.currentUser(user);

      var urlTenant = context.currentTenant();

      if (StringUtils.isNotBlank(princ.getTenantId())) {
        // ????????????????????????
        var tenant = tenantService.findTenantById(princ.getTenantId());
        Assert.state(tenant != null, "tenant[id={}] not found", princ.getTenantId());
        // ???????????????????????????URL??????????????????
        if (userType.isTenantUser() && Objects.nonNull(urlTenant)) {
          Assert.state(tenant.getId().equals(urlTenant.getId()), "tenant[id={}] not match",
              princ.getTenantId());
        }
      }
      // ???tokenId??????sessionId
      var sessionId = tokenService.generateTokenId(princ);
      return new DinoAuthSession(sessionId, user, authzInfoProvider);
    } catch (IOException e) {
      log.error("error occured while create AuthSession from[{}]", prinStr, e);
      return null;
    }
  }

  @Override
  public void closeSession(HttpServletRequest request, Object session) {
    // ???????????????????????????
    context.currentUser(null);
  }

  public static class DinoAuthSession implements AuthSession {

    private final String sessionId;

    private final User<?> user;

    private final Supplier<List<Permission>> permissions;

    private final Supplier<Set<String>> roles;

    /**
     * @param user
     */
    public DinoAuthSession(String sessionId, User<?> user, AuthInfoProvider<User<?>> authInfoService) {
      this.sessionId = sessionId;
      this.user = user;
      this.permissions = Suppliers.<List<Permission>>lazy(
          () -> {
            var perms = authInfoService.getPermissions(user);
            if (Objects.isNull(perms)) {
              return List.of();
            }
            return List.copyOf(perms);
          });

      this.roles = Suppliers.lazy(() -> new HashSet<>(authInfoService.getRoles(user)));
    }

    @Override
    public String getSessionId() {
      return sessionId;
    }

    @Override
    public boolean isLogin() {
      return Objects.nonNull(user);
    }

    @Override
    public boolean isLoginAs(String subjectType) {
      return isLogin() && user.getUserType().getType().equals(subjectType);
    }

    @Override
    public String getSubjectId() {
      return Objects.isNull(user) ? null : user.getId().toString();
    }

    @Override
    public String getSubjectType() {
      if (isLogin()) {
        return user.getUserType().getType();
      }
      return null;
    }

    @Override
    public Collection<String> getSubjectRoles() {
      if (isLogin()) {
        return roles.get();
      }
      return Collections.emptyList();
    }

    @Override
    public Collection<Permission> getSubjectPermissions() {
      if (isLogin()) {
        return permissions.get();
      }
      return Collections.emptyList();
    }

  }

}
