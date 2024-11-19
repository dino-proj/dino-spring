// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login.tenanted;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.dinodev.spring.commons.request.PostBody;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import cn.dinodev.spring.core.modules.login.LoginAuth;
import cn.dinodev.spring.core.sys.token.TokenPrincaple;
import cn.dinodev.spring.core.sys.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author Cody Lu
 * @date 2022-06-02 16:45:11
 */

public interface LoginByRefreshtokenTenanted<U extends User<K>, K extends Serializable>
    extends LoginControllerBaseTenanted<U, K> {

  /**
   * 用户名密码登录
   * @param tenant
   * @param req
   * @return
   */
  @Operation(summary = "Refreshtoken登录")
  @ParamTenant
  @PostMapping("/refreshtoken")
  default Response<LoginAuth<U, K>> byRefreshToken(Tenant tenant,
      @RequestBody @Validated PostBody<RefreshtokenLoginBody> req) {

    Assert.notNull(tenant, Status.CODE.FAIL_TENANT_NOT_EXIST);

    var body = req.getBody();
    var userType = userServiceProvider().resolveUserType(body.getUserType());

    UserService<U, K> userService = userServiceProvider().resolveUserService(userType);
    U user = userService.getUserById(userType, body.getUserId()).orElse(null);
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    var pric = TokenPrincaple.builder().userId(body.getUserId()).userType(body.getUserType()).plt(req.getPlt())
        .guid(req.getGuid()).tenantId(tenant.getId()).build();
    var newToken = tokenService().refreshLoginToken(pric, user.getSecretKey(), body.refreshToken).orElse(null);
    Assert.notNull(newToken, Status.fail("refreshtoken失败"));

    var auth = newLoginAuth();
    auth.setUser(user);
    auth.setAuthToken(newToken);
    auth.setCurrentTenant(tenant);

    return Response.success(auth);
  }

  @Data
  public static class RefreshtokenLoginBody {
    @Schema(description = "用户类型", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "用户类型不能为空")
    private String userType;

    @Schema(description = "用户ID", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @Schema(description = "Refresh Token", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Refresh Token不能为空")
    private String refreshToken;
  }
}