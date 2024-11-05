// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.sms;

import jakarta.annotation.Nullable;

/**
 * 短信验证码服务
 * @author Cody Lu
 * @date 2024-03-20 14:40:18
 */

public interface SmsCaptchaService {

  /**
   * 发送短信验证码
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param captcha 短信验证码
   * @return 发送成功返回true，否则返回false
   */
  default boolean sendCaptcha(String mobile, String captcha) {
    return sendCaptcha(mobile, captcha, null);
  }

  /**
   * 发送短信验证码
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param captcha 短信验证码
   * @param signName 短信签名, 可以为空, 为空时使用配置中的默认签名
   * @return 发送成功返回true，否则返回false
   */
  boolean sendCaptcha(String mobile, String captcha, @Nullable String signName);

  /**
    * 发送短信验证码
    * @param templateId 短信模板的ID
    * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
    * @param captcha 短信验证码
    * @param signName 短信签名, 可以为空, 为空时使用配置中的默认签名
    * @return 发送成功返回true，否则返回false
    */
  boolean sendCaptcha(String templateId, String mobile, String captcha, @Nullable String signName);

  /**
   * 发送短信验证码, 验证码长度使用配置中的默认长度
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @return 发送成功返回验证码，否则返回null
   */
  String sendCaptcha(String mobile);

  /**
   * 发送短信验证码
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param length 验证码长度
   * @return 发送成功返回验证码，否则返回null
   */
  @Nullable
  String sendCaptcha(String mobile, int length);

  /**
   * 验证短信验证码
   * @param mobile 手机号
   * @param captcha 验证码
   * @return 验证成功返回true，否则返回false
   */
  boolean verifyCaptcha(String mobile, @Nullable String captcha);

}