// Copyright 2021 dinodev.cn
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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.annotion.param.ParamTenant;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

/**
 *
 * @author Cody LU
 */

public interface LoginByUserName<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
   * 用户名密码登录
   * @param tenantId
   * @param req
   * @return
   */
  @Operation(summary = "用户名密码登录")
  @ParamTenant
  @PostMapping("/username")
  default Response<LoginAuth<U, K>> byUserName(@PathVariable("tenant_id") String tenantId,
      @RequestBody PostBody<UserNameLoginBody> req) {
    if (Tenant.isSys(tenantId)) {
      return Response.fail(Status.CODE.FAIL_TENANT_NOT_EXIST);
    }
    var tenant = tenantService().getById(tenantId, Tenant.class);
    Assert.notNull(tenant, Status.CODE.FAIL_TENANT_NOT_EXIST);
    //查询用户
    var username = req.getBody().getUsername();
    //通过用户名登录，如果用户使用手机号作为登录名，则和其他用户相同的手机号字段冲突，登录的时候存在不确定性
    U user = loginService().findUserByLoginName(tenant.getId(), username).orElse(null);
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    //验证用户密码
    Assert.isTrue(loginService().verifyUserPassword(user, req.getBody().getPassword()),
        Status.CODE.FAIL_INVALID_PASSWORD);
    //返回授权签名
    return Response.success(loginService().loginAuth(tenant,
        tenantService().projection(loginService().userClass(), user), req.getPlt(), req.getGuid()));
  }

  @Data
  public static class UserNameLoginBody {
    @Schema(description = "用户名", requiredMode = RequiredMode.REQUIRED)
    @Size(min = 4)
    @NotBlank
    private String username;

    @NotBlank
    @Schema(description = "登录密码", requiredMode = RequiredMode.REQUIRED)
    @Size(min = 6)
    @NotBlank
    private String password;
  }
}
