// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.dinodev.spring.commons.request.PostBody;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */

public interface LoginByUserName<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
   * 用户名密码登录
   * @param req
   * @return
   */
  @Operation(summary = "用户名密码登录")
  @PostMapping("/username")
  default Response<LoginAuth<U, K>> byUserName(@RequestBody PostBody<UserNameLoginBody> req) {

    //查询用户
    var username = req.getBody().getUsername();
    //通过用户名登录，如果用户使用手机号作为登录名，则和其他用户相同的手机号字段冲突，登录的时候存在不确定性
    U user = findUserByLoginName(username).orElse(null);
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    //验证用户密码
    Assert.isTrue(verifyUserPassword(user, req.getBody().getPassword()),
        Status.CODE.FAIL_INVALID_PASSWORD);
    //返回授权签名
    return Response.success(loginAuth(user, req.getPlt(), req.getGuid()));
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

  /**
   * 验证用户密码
   * @param user
   * @param password
   * @return
   */
  default boolean verifyUserPassword(U user, String password) {
    var signToken = tokenService().siginParams(user.getSecretKey(),
        Map.of("username", user.getLoginName(), "password", password));

    return user.getPasswordHash().equalsIgnoreCase(signToken);
  }

  /**
   * 根据用户名查找用户
   *
   * @param username
   * @return
   */
  Optional<U> findUserByLoginName(String username);

}
