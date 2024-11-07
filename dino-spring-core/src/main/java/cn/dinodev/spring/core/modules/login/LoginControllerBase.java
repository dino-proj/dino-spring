// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login;

import java.io.Serializable;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.projection.ProjectionService;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.core.sys.token.TokenService;
import cn.dinodev.spring.core.sys.user.UserServiceProvider;
import org.slf4j.Logger;

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
   * User class
   * @return
   */
  default Class<U> userClass() {
    return TypeUtils.getGenericParamClass(this, LoginControllerBase.class, 1);
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
  ProjectionService projectionService();
}
