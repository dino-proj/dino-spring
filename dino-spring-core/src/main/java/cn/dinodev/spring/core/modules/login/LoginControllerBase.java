// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login;

import java.io.Serializable;

import org.slf4j.Logger;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.projection.ProjectionService;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.core.entity.Code;
import cn.dinodev.spring.core.modules.login.config.LoginModuleProperties;
import cn.dinodev.spring.core.sys.token.TokenPrincaple;
import cn.dinodev.spring.core.sys.token.TokenService;
import cn.dinodev.spring.core.sys.user.UserService;
import cn.dinodev.spring.core.sys.user.UserServiceProvider;

/**
 *
 * @author Cody Lu
 */

public interface LoginControllerBase<U extends User<K>, K extends Serializable> {

  /**
   * Logger
   * @return
   */
  Logger log();

  /**
   * UserService
   * @return
   */
  UserService<U, K> userService();

  /**
   * User class
   * @return
   */
  default Class<U> userClass() {
    return TypeUtils.getGenericParamClass(this, LoginControllerBase.class, 1);
  }

  /**
   * 获取登录配置
   * @return
   */
  default LoginModuleProperties loginModuleProperties() {
    return ContextHelper.findBean(LoginModuleProperties.class);
  }

  /**
   * token Service
   * @return
   */
  default TokenService tokenService() {
    return ContextHelper.findBean(TokenService.class);
  }

  /**
   * 用户Service Provider
   * @return
   */
  default UserServiceProvider userServiceProvider() {
    return ContextHelper.findBean(UserServiceProvider.class);
  }

  /**
   * 投影服务
   * @return
   */
  default ProjectionService projectionService() {
    return ContextHelper.findBean(ProjectionService.class);
  }

  /**
   * 生成LoginAuth对象
   * @return
   */
  default LoginAuth<U, K> newLoginAuth() {
    return new LoginAuth<>();
  }

  /**
   * 对用户登录生成授权Token
   * @param user
   * @param plt
   * @param guid
   * @return
   */
  default LoginAuth<U, K> loginAuth(U user, String plt, String guid) {
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    Assert.isTrue(user.getStatus().equals(Code.STATUS.OK.name().toLowerCase()), Status.CODE.FAIL_LOGIN_DENNY);

    var auth = this.newLoginAuth();
    auth.setUser(user);

    var princ = TokenPrincaple.builder()//
        .userId(String.valueOf(user.getId()))//
        .userType(user.getUserType().getType())//
        .guid(guid).plt(plt).build();

    auth.setAuthToken(this.tokenService().genLoginToken(princ, user.getSecretKey()));
    this.userService().onLogin(user.getUserType(), String.valueOf(user.getId()));
    return auth;
  }

}
