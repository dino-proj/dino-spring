// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.commons.utils.ValidateUtil;
import cn.dinodev.spring.core.entity.Code;
import cn.dinodev.spring.core.modules.login.config.LoginModuleProperties;
import cn.dinodev.spring.core.modules.sms.SmsCaptchaService;
import cn.dinodev.spring.core.sys.token.TokenPrincaple;
import cn.dinodev.spring.core.sys.token.TokenService;
import cn.dinodev.spring.core.sys.user.UserService;

/**
 *
 * @author Cody Lu
 */

public abstract interface LoginServiceBase<U extends User<K>, K extends Serializable> {

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
    return TypeUtils.getGenericParamClass(this, LoginServiceBase.class, 0);
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
  default LoginAuth<U, K> newLoginAuth() {
    return new LoginAuth<>();
  }

  /**
   * 检查mock登录
   * @param mobile 手机号
   * @param captcha 短信验证码
   * @return
   */
  default boolean verifyMockLogin(String mobile, String captcha) {
    return CollectionUtils.containsAny(this.loginModuleProperties().getMock().getMobiles(), mobile)
        && captcha.equals(this.loginModuleProperties().getMock().getCaptcha());
  }

  /**
   * 是否是模拟登录，检查是否将此手机号加入到模拟登录列表中
   * @param mobile 手机号
   * @return
   */
  default boolean isMockLogin(String mobile) {
    return CollectionUtils.containsAny(this.loginModuleProperties().getMock().getMobiles(), mobile);
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
    if (this.verifyMockLogin(mobile, captcha)) {
      return true;
    }

    return smsService().verifyCaptcha(mobile, captcha);
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
   *
   * @param username
   * @return
   */
  Optional<U> findUserByLoginName(String username);

  /**
   * 根据手机号码查询用户
   *
   * @param mobile
   * @return
   */
  Optional<U> findUserByMobile(String mobile);

}
