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

package org.dinospring.core.modules.login;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.dinospring.core.sys.user.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 * @date 2022-06-02 16:45:11
 */

public interface LoginByRefreshtoken<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
   * 用户名密码登录
   * @param tenantId
   * @param req
   * @return
   */
  @Operation(summary = "Refreshtoken登录")
  @ParamTenant
  @PostMapping("/refreshtoken")
  default Response<LoginAuth<U, K>> byRefreshToken(@PathVariable("tenant_id") String tenantId,
      @RequestBody @Validated PostBody<RefreshtokenLoginBody> req) {
    var tenant = tenantService().getById(tenantId, Tenant.class);
    Assert.notNull(tenant, Status.CODE.FAIL_TENANT_NOT_EXIST);

    var body = req.getBody();
    var userType = userServiceProvider().resolveUserType(body.getUserType());

    UserService<U, K> userService = userServiceProvider().resolveUserService(userType);
    U user = userService.getUserById(userType, body.getUserId()).orElse(null);
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    var pric = TokenPrincaple.builder().userId(body.getUserId()).userType(body.getUserType()).plt(req.getPlt())
        .guid(req.getGuid()).tenantId(tenantId).build();
    var newToken = tokenService().refreshLoginToken(pric, user.getSecretKey(), body.refreshToken).orElse(null);
    Assert.notNull(newToken, Status.fail("refreshtoken失败"));

    var auth = loginService().newLoginAuth();
    auth.setUser(user);
    auth.setAuthToken(newToken);
    auth.setCurrentTenant(tenant);

    return Response.success(auth);
  }

  @Data
  public static class RefreshtokenLoginBody {
    @Schema(description = "用户类型", required = true)
    @NotBlank(message = "用户类型不能为空")
    private String userType;

    @Schema(description = "用户ID", required = true)
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @Schema(description = "Refresh Token", required = true)
    @NotBlank(message = "Refresh Token不能为空")
    private String refreshToken;
  }
}
