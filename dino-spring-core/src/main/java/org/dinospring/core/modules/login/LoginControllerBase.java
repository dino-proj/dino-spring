// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.login;

import java.io.Serializable;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.projection.ProjectionService;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.sys.user.UserServiceProvider;
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
