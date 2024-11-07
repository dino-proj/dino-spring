// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.auth.Permission;
import cn.dinodev.spring.auth.session.AuthInfoProvider;
import cn.dinodev.spring.auth.session.AuthSession;
import cn.dinodev.spring.auth.session.AuthSessionResolver;
import cn.dinodev.spring.commons.context.DinoContext;
import cn.dinodev.spring.commons.function.Suppliers;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.core.security.DinoAuthSessionResolver.DinoAuthSession;
import cn.dinodev.spring.core.sys.tenant.TenantService;
import cn.dinodev.spring.core.sys.token.Token;
import cn.dinodev.spring.core.sys.token.TokenPrincaple;
import cn.dinodev.spring.core.sys.token.TokenService;
import cn.dinodev.spring.core.sys.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2022-04-10 18:56:55
 */

@Slf4j
public class DinoAuthSessionResolver implements AuthSessionResolver<DinoAuthSession> {

  @Autowired
  private TokenService tokenService;

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
    String authzHeader = request.getHeader(this.getAuthHeader());

    if (StringUtils.isBlank(authzHeader)) {
      return null;
    }

    var prinStr = Token.extractPrinc(authzHeader);
    var tokenStr = Token.extractToken(authzHeader);

    try {
      var princ = this.objectMapper.readValue(Base64.getUrlDecoder().decode(prinStr), TokenPrincaple.class);
      log.info("req http:{}", request.getRequestURL());
      // 验证token是否有效
      Assert.isTrue(this.tokenService.checkLoginToken(princ, tokenStr), Status.CODE.FAIL_INVALID_AUTH_TOKEN);

      // 获取用户Service
      var userType = this.userServiceProvider.resolveUserType(princ.getUserType());
      Assert.notNull(userType, "user type not found");
      var userService = this.userServiceProvider.resolveUserService(userType);
      // 获取用户信息
      var user = userService.getUserById(userType, princ.getUserId()).orElse(null);
      Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

      this.context.currentUser(user);

      var urlTenant = this.context.currentTenant();

      if (StringUtils.isNotBlank(princ.getTenantId())) {
        // 检查租户是否存在
        var tenant = this.tenantService.findTenantById(princ.getTenantId());
        Assert.state(tenant != null, "tenant[id={}] not found", princ.getTenantId());
        // 检查当前租户是否跟URL中的租户一致
        if (userType.isTenantUser() && Objects.nonNull(urlTenant)) {
          Assert.state(tenant.getId().equals(urlTenant.getId()), "tenant[id={}] not match",
              princ.getTenantId());
        }
      }
      // 用tokenId作为sessionId
      var sessionId = this.tokenService.generateTokenId(princ);
      return new DinoAuthSession(sessionId, user, this.authzInfoProvider);
    } catch (IOException e) {
      log.error("error occured while create AuthSession from[{}]", prinStr, e);
      return null;
    }
  }

  @Override
  public void closeSession(HttpServletRequest request, Object session) {
    // 将当前登录用户清空
    this.context.currentUser(null);
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
      return this.sessionId;
    }

    @Override
    public boolean isLogin() {
      return Objects.nonNull(this.user);
    }

    @Override
    public boolean isLoginAs(String subjectType) {
      return this.isLogin() && this.user.getUserType().getType().equals(subjectType);
    }

    @Override
    public String getSubjectId() {
      return Objects.isNull(this.user) ? null : this.user.getId().toString();
    }

    @Override
    public String getSubjectType() {
      if (this.isLogin()) {
        return this.user.getUserType().getType();
      }
      return null;
    }

    @Override
    public Collection<String> getSubjectRoles() {
      if (this.isLogin()) {
        return this.roles.get();
      }
      return Collections.emptyList();
    }

    @Override
    public Collection<Permission> getSubjectPermissions() {
      if (this.isLogin()) {
        return this.permissions.get();
      }
      return Collections.emptyList();
    }

  }

}
