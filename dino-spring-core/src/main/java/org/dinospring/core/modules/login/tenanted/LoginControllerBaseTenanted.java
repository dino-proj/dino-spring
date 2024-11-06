// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.login.tenanted;

import java.io.Serializable;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.sys.User;
import org.dinospring.core.modules.login.LoginControllerBase;
import org.dinospring.core.sys.tenant.TenantService;

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
