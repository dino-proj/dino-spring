// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.login.tenanted;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.commons.utils.ValidateUtil;
import org.dinospring.core.entity.Code;
import org.dinospring.core.modules.login.config.LoginModuleProperties;
import org.dinospring.core.modules.sms.SmsCaptchaService;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.sys.user.UserService;

/**
 *
 * @author Cody Lu
 */

public abstract interface LoginServiceBaseTenanted<U extends User<K>, K extends Serializable> {

  /**
   * UserService
   * @return
   */
  UserService<U, K> userService();

  /**
   * TokenService
   * @return
   */
  default TokenService tokenService() {
    return ContextHelper.findBean(TokenService.class);
  }

  /**
   * SmsService
   * @return
   */
  default SmsCaptchaService smsService() {
    return ContextHelper.findBean(SmsCaptchaService.class);
  }

  /**
   * User class
   * @return
   */
  default Class<U> userClass() {
    return TypeUtils.getGenericParamClass(this, LoginServiceBaseTenanted.class, 0);
  }

  /**
   * 获取登录配置
   * @return
   */
  default LoginModuleProperties loginModuleProperties() {
    return ContextHelper.findBean(LoginModuleProperties.class);
  }

  /**
   * 生成LoginAuth对象
   * @return
   */
  default LoginAuthTenanted<U, K> newLoginAuth() {
    return new LoginAuthTenanted<>();
  }

  /**
   * 是否是模拟登录,默认根据配置信息
   * @param mobile 手机号
   * @param captcha 短信验证码
   * @return
   */
  default boolean isMockLogin(String mobile, String captcha) {
    return CollectionUtils.containsAny(this.loginModuleProperties().getMock().getMobiles(), mobile)
        && captcha.equals(this.loginModuleProperties().getMock().getCaptcha());
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

  /**
   * 验证手机号和短信验证码
   * @param mobile
   * @param captcha
   * @return
   */
  default boolean verifySmsCaptcha(String mobile, String captcha) {
    //手机号非法
    Assert.isTrue(ValidateUtil.isMobile(mobile), Status.CODE.FAIL_INVALID_PHONE);
    //验证码非空，验证码过期，验证码不一致
    Assert.hasText(captcha, Status.invalidParam("手机验证码不能为空!"));

    //检查能否模拟登录
    if (this.isMockLogin(mobile, captcha)) {
      return true;
    }

    return this.smsService().verifyCaptcha(mobile, captcha);
  }

  /**
   * 验证用户密码
   * @param user
   * @param password
   * @return
   */
  default boolean verifyUserPassword(U user, String password) {
    var signToken = this.tokenService().siginParams(user.getSecretKey(),
        Map.of("username", user.getLoginName(), "password", password));

    return user.getPasswordHash().equalsIgnoreCase(signToken);
  }

  /**
   * 发送手机短信验证码
   * @param mobile
   * @return
   */
  default boolean sendSmsCaptcha(String mobile) {
    var captcha = this.smsService().sendCaptcha(mobile);
    return Objects.nonNull(captcha);
  }

  /**
   * 根据用户名查找用户
   * @param tenantId
   * @param username
   * @return
   */
  Optional<U> findUserByLoginName(String tenantId, String username);

  /**
   * 根据手机号码查询用户
   *
   * @param tenantId
   * @param mobile
   * @return
   */
  Optional<U> findUserByMobile(String tenantId, String mobile);

}
