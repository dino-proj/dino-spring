// Copyright 2021 dinospring.cn
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
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.commons.utils.ValidateUtil;
import org.dinospring.core.entity.Code;
import org.dinospring.core.modules.login.config.LoginModuleProperties;
import org.dinospring.core.modules.message.sms.SmsService;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.sys.user.UserService;

/**
 *
 * @author tuuboo
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
  default SmsService smsService() {
    return ContextHelper.findBean(SmsService.class);
  }

  /**
   * User class
   * @return
   */
  default Class<U> userClass() {
    return TypeUtils.getGenericParamClass(this, LoginServiceBase.class, 0);
  }

  /**
   * ??????????????????
   * @return
   */
  default LoginModuleProperties loginModuleProperties() {
    return ContextHelper.findBean(LoginModuleProperties.class);
  }

  /**
   * ??????LoginAuth??????
   * @return
   */
  default LoginAuth<U, K> newLoginAuth() {
    return new LoginAuth<>();
  }

  /**
   * ?????????????????????,????????????????????????
   * @param mobile ?????????
   * @param captcha ???????????????
   * @return
   */
  default boolean isMockLogin(String mobile, String captcha) {
    return CollectionUtils.containsAny(loginModuleProperties().getMock().getMobiles(), mobile)
        && captcha.equals(loginModuleProperties().getMock().getCaptcha());
  }

  /**
   * ???????????????????????????Token
   * @param tenant
   * @param user
   * @param plt
   * @param guid
   * @return
   */
  default LoginAuth<U, K> loginAuth(Tenant tenant, U user, String plt, String guid) {
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    Assert.isTrue(user.getStatus().equals(Code.STATUS.OK.name().toLowerCase()), Status.CODE.FAIL_LOGIN_DENNY);

    var auth = newLoginAuth();
    auth.setUser(user);
    auth.setCurrentTenant(tenant);

    var princ = TokenPrincaple.builder().tenantId(tenant.getId())//
        .userId(String.valueOf(user.getId()))//
        .userType(user.getUserType().getType())//
        .guid(guid).plt(plt).build();

    auth.setAuthToken(tokenService().genLoginToken(princ, user.getSecretKey()));
    userService().onLogin(user.getUserType(), String.valueOf(user.getId()));
    return auth;
  }

  /**
   * ?????????????????????????????????
   * @param mobile
   * @param captcha
   * @return
   */
  default boolean verifySmsCaptcha(String mobile, String captcha) {
    //???????????????
    Assert.isTrue(ValidateUtil.isMobile(mobile), Status.CODE.FAIL_INVALID_PHONE);
    //??????????????????????????????????????????????????????
    Assert.hasText(captcha, Status.invalidParam("???????????????????????????!"));

    //????????????????????????
    if (isMockLogin(mobile, captcha)) {
      return true;
    }

    return StringUtils.equals(retriveSmsCaptcha(mobile), captcha);
  }

  /**
   * ??????????????????
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
   * ???????????????????????????
   * @param mobile
   * @return
   */
  default boolean sendSmsCaptcha(String mobile) {
    var captcha = new RandomStringGenerator.Builder().withinRange('0', '9').build().generate(4);
    if (smsService().sendSmsCaptcha(mobile, captcha, null)) {
      saveSmsCaptcha(mobile, captcha);
      return true;
    }
    return false;
  }

  /**
   * ???????????????????????????
   * @param tenantId
   * @param username
   * @return
   */
  Optional<U> findUserByLoginName(String tenantId, String username);

  /**
   * ??????????????????????????????
   *
   * @param tenantId
   * @param mobile
   * @return
   */
  Optional<U> findUserByMobile(String tenantId, String mobile);

  /**
   * ???????????????
   * @param mobile
   * @param captcha
   */
  void saveSmsCaptcha(String mobile, String captcha);

  /**
   * ???????????????
   * @param mobile
   * @return
   */
  String retriveSmsCaptcha(String mobile);
}
