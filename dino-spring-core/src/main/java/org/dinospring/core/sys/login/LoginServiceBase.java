package org.dinospring.core.sys.login;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import com.botbrain.dino.utils.ValidateUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.entity.Code;
import org.dinospring.core.modules.message.sms.SmsService;
import org.dinospring.core.sys.org.UserEntityBase;
import org.dinospring.core.sys.token.TokenPrincaple;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.sys.user.UserService;
import org.springframework.data.util.CastUtils;

public abstract interface LoginServiceBase<U extends UserEntityBase<K>, V extends User<K>, K extends Serializable> {

  UserService<V, K> userService();

  TokenService tokenService();

  SmsService smsService();

  default Class<V> userClass() {
    var tp = this.getClass().getGenericSuperclass();
    if (tp instanceof ParameterizedType) {
      var paramType = (ParameterizedType) tp;
      return CastUtils.cast(paramType.getActualTypeArguments()[1]);
    }
    return null;
  }

  default LoginAuth<V, K> newLoginAuth() {
    return new LoginAuth<>();
  }

  /**
   * 是否是模拟登录
   * @param mobile 手机号
   * @param captcha 短信验证码
   * @return
   */
  default boolean isMockLogin(String mobile, String captcha) {
    return false;
  }

  /**
   * 对用户登录生成授权Token
   * @param tenant
   * @param user
   * @param plt
   * @param guid
   * @return
   */
  default LoginAuth<V, K> loginAuth(Tenant tenant, V user, String plt, String guid) {
    Assert.notNull(user, Status.CODE.FAIL_USER_NOT_EXIST);

    Assert.isTrue(user.getStatus() == Code.STATUS.OK.getId(), Status.CODE.FAIL_LOGIN_DENNY);

    var auth = newLoginAuth();
    auth.setUser(user);
    auth.setCurrentTenant(tenant);

    var princ = TokenPrincaple.builder().tenantId(tenant.getId())//
        .userId(String.valueOf(user.getId()))//
        .userType(user.getUserType().getType())//
        .guid(guid).plt(plt).build();

    auth.setAuthToken(tokenService().genLoginToken(princ, user.getSecretKey()));
    userService().onLogin(user.getUserType().getType(), String.valueOf(user.getId()));
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
    if (isMockLogin(mobile, captcha)) {
      return true;
    }

    return StringUtils.equals(retriveSmsCaptcha(mobile), captcha);
  }

  default boolean verifyUserPassword(U user, String password) {
    var signToken = tokenService().siginParams(user.getSecretKey(),
        Map.of("username", user.getLoginName(), "password", password));

    return user.getPasswordHash().equalsIgnoreCase(signToken);
  }

  default boolean sendSmsCaptcha(String mobile) {
    var captcha = new RandomStringGenerator.Builder().withinRange('0', '9').build().generate(4);
    if (smsService().sendSmsCaptcha(mobile, captcha, null)) {
      saveSmsCaptcha(mobile, captcha);
      return true;
    }
    return false;
  }

  void saveSmsCaptcha(String mobile, String captcha);

  String retriveSmsCaptcha(String mobile);
}
