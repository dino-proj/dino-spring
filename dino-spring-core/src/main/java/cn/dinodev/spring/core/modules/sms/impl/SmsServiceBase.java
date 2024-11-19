// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.sms.impl;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import cn.dinodev.spring.core.modules.sms.SmsCaptchaService;
import cn.dinodev.spring.core.modules.sms.SmsService;
import cn.dinodev.spring.core.modules.sms.config.SmsModuleProperties;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
public abstract class SmsServiceBase implements SmsService, SmsCaptchaService {

  @Autowired(required = false)
  private StringRedisTemplate captchaRedisTemplate;

  @Autowired
  private SmsModuleProperties smsModuleProperties;

  @Override
  public boolean sendCaptcha(String mobile, String captcha, String signName) {
    return this.sendCaptcha(this.smsModuleProperties.getCaptcha().getTemplateId(), mobile, captcha, signName);
  }

  @Override
  public String sendCaptcha(String mobile) {
    return this.sendCaptcha(mobile, this.smsModuleProperties.getCaptcha().getLength());
  }

  @Override
  public String sendCaptcha(String mobile, int length) {
    String captcha = String.valueOf(RandomStringUtils.secure().nextNumeric(length));
    if (this.sendCaptcha(mobile, captcha, this.smsModuleProperties.getCaptcha().getSignName())) {
      return captcha;
    } else {
      return null;
    }
  }

  @Override
  public boolean sendCaptcha(String templateId, String mobile, String captcha, String signName) {
    Duration expire = this.smsModuleProperties.getCaptcha().getExpire();
    Long minutes = expire.toMinutes();
    if (this.sendTemplateSms(mobile, templateId, List.of(captcha, minutes.toString()), signName)) {
      if (this.captchaRedisTemplate != null) {
        this.captchaRedisTemplate.opsForValue().set(mobile, captcha, minutes, TimeUnit.MINUTES);
      } else if (log.isDebugEnabled()) {
        log.warn("验证码发送成功，但是未配置Redis，无法保存验证码");
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean verifyCaptcha(String mobile, String captcha) {
    if (this.captchaRedisTemplate == null) {
      log.error("未配置Redis，无法验证验证码");
      return false;
    }
    String savedCaptcha = this.captchaRedisTemplate.opsForValue().get(this.getCaptchaRedisKey(mobile));
    return captcha.equals(savedCaptcha);
  }

  /**
   * 获取Captcha Redis Key
   * @param mobile 手机号
   * @return Redis Key
   */
  protected String getCaptchaRedisKey(String mobile) {
    return "sms:captcha:" + mobile;
  }

}
