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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.Permission;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.auth.session.AuthSessionHttpResolver;
import org.dinospring.auth.support.WildcardPermission;
import org.dinospring.commons.context.DinoContext;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.UserType;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.security.DinoAuthSessionResolver.DinoAuthSession;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.core.sys.token.Token;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.sys.user.UserService;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 * @date 2022-04-10 18:56:55
 */

@Slf4j
public class DinoAuthSessionResolver implements AuthSessionHttpResolver<DinoAuthSession> {

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
      // 验证token是否有效
      Assert.isTrue(tokenService.checkLoginToken(princ, tokenStr), Status.CODE.FAIL_INVALID_AUTH_TOKEN);

      // 获取用户Service
      var userType = userServiceProvider.resolveUserType(princ.getUserType());
      Assert.notNull(userType, "user type not found");
      var userService = userServiceProvider.resolveUserService(userType);
      // 获取用户信息
      var user = userService.getUserById(userType, princ.getUserId()).orElse(null);
      Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

      var urlTenant = context.currentTenant();

      if (StringUtils.isNotBlank(princ.getTenantId())) {
        // 检查租户是否存在
        var tenant = tenantService.findTenantById(princ.getTenantId());
        Assert.state(tenant != null, "tenant[id={}] not found", princ.getTenantId());
        // 检查当前租户是否跟URL中的租户一致
        if (userType.isTenantUser() && Objects.nonNull(urlTenant)) {
          Assert.state(tenant.getId().equals(urlTenant.getId()), "tenant[id={}] not match",
              princ.getTenantId());
        }
      }
      // 用tokenId作为sessionId
      var sessionId = tokenService.generateTokenId(princ);
      return new DinoAuthSession(sessionId, userType, user.getId().toString(), userService);
    } catch (IOException e) {
      log.error("error occured while create AuthSession from[{}]", prinStr, e);
      return null;
    }
  }

  public static class DinoAuthSession implements AuthSession {

    private final String sessionId;

    private final UserType userType;

    private final String userId;

    private final Lazy<List<Permission>> permissions;

    private final Lazy<Set<String>> roles;

    /**
     * @param user
     */
    public DinoAuthSession(String sessionId, UserType userType, String userId, UserService<?, ?> userService) {
      this.sessionId = sessionId;
      this.userType = userType;
      this.userId = userId;
      this.permissions = Lazy.of(
          new Supplier<List<Permission>>() {
            @Override
            public List<Permission> get() {
              var perms = userService.getPermissions(userType, userId);
              if (Objects.isNull(perms)) {
                return List.of();
              }
              return perms.stream().map(WildcardPermission::of).collect(Collectors.toList());
            }
          });

      this.roles = Lazy.of(() -> userService.getRoles(userType, userId));
    }

    @Override
    public String getSessionId() {
      return sessionId;
    }

    @Override
    public boolean isLogin() {
      return Objects.nonNull(userId);
    }

    @Override
    public boolean isLoginAs(String subjectType) {
      return isLogin() && userType.getType().equals(subjectType);
    }

    @Override
    public String getSubjectId() {
      return userId;
    }

    @Override
    public String getSubjectType() {
      if (isLogin()) {
        return userType.getType();
      }
      return null;
    }

    @Override
    public Collection<String> getSubjectRoles() {
      if (isLogin()) {
        return roles.get();
      }
      return null;
    }

    @Override
    public Collection<Permission> getSubjectPermissions() {
      if (isLogin()) {
        return permissions.get();
      }
      return null;
    }

  }

}
