// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login.tenanted;

import java.io.Serializable;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.core.entity.Code;
import cn.dinodev.spring.core.modules.login.LoginControllerBase;
import cn.dinodev.spring.core.sys.tenant.TenantService;
import cn.dinodev.spring.core.sys.token.TokenPrincaple;

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
   * 生成LoginAuth对象
   * @return
   */
  default LoginAuthTenanted<U, K> newLoginAuth() {
    return new LoginAuthTenanted<>();
  }

  /**
   * 对用户登录生成授权Token
   * @param tenant
   * @param user
   * @param plt
   * @param guid
   * @return
   */
  default LoginAuthTenanted<U, K> loginAuth(Tenant tenant, U user, String plt, String guid) {
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    Assert.isTrue(user.getStatus().equals(Code.STATUS.OK.name().toLowerCase()), Status.CODE.FAIL_LOGIN_DENNY);

    var auth = this.newLoginAuth();
    auth.setUser(user);
    auth.setCurrentTenant(tenant);

    var princ = TokenPrincaple.builder().tenantId(tenant.getId())//
        .userId(String.valueOf(user.getId()))//
        .userType(user.getUserType().getType())//
        .guid(guid).plt(plt).build();

    auth.setAuthToken(this.tokenService().genLoginToken(princ, user.getSecretKey()));
    this.userService().onLogin(user.getUserType(), String.valueOf(user.getId()));
    return auth;
  }
}
