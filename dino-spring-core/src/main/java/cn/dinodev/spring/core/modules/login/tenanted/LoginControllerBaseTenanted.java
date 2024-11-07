// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login.tenanted;

import java.io.Serializable;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.core.modules.login.LoginControllerBase;
import cn.dinodev.spring.core.sys.tenant.TenantService;

/**
 *
 * @author Cody Lu
 */

public interface LoginControllerBaseTenanted<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
   * 租户Service
   * @return
   */
  default TenantService tenantService() {
    return ContextHelper.findBean(TenantService.class);
  }

  /**
   * 登录Service
   * @return
   */
  LoginServiceBaseTenanted<U, K> loginService();
}
