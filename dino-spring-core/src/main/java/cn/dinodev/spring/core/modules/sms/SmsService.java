// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.sms;

import java.util.Collection;
import java.util.List;

import jakarta.annotation.Nullable;

/**
 *
 * @author Cody Lu
 */

public interface SmsService {

  /**
   * 批量发送模版短信消息
   * @param mobiles 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param templateId 短信模板的ID
   * @param templateParams 短信模板参数
   * @param signName 短信签名, 可以为空, 为空时使用配置中的默认签名
   * @return 返回发送失败的手机号码列表
   */
  Collection<String> sendTemplateSms(Collection<String> mobiles, String templateId, Object templateParams,
      @Nullable String signName);

  /**
   * 发送模版短信消息
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param templateId 短信模板的ID
   * @param templateParams 短信模板参数
   * @param signName 短信签名, 可以为空, 为空时使用配置中的默认签名
   * @return 发送成功返回true，否则返回false
   */
  default boolean sendTemplateSms(String mobile, String templateId, Object templateParams,
      @Nullable String signName) {
    return this.sendTemplateSms(List.of(mobile), templateId, templateParams, signName).isEmpty();
  }

  /**
   * 发送短信
   * @param mobile 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param message 短信内容
   * @return 发送成功返回true，否则返回false
   */
  default boolean sendSms(String mobile, String message) {
    return this.sendSms(List.of(mobile), message);
  }

  /**
   * 批量发送短信
   * @param mobiles 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。默认使用+86
   * @param message 短信内容
   * @return 发送成功返回true，否则返回false
   */
  boolean sendSms(Collection<String> mobiles, String message);
}
