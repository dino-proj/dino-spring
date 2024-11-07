// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.sms.config;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Data;

/**
 * 阿里云短信配置
 * @author Cody Lu
 * @date 2024-03-20 14:37:29
 */

@Data
public class AliSmsProperties {

  /**
  * 端点
  * Endpoint
  *
  * 通常是不需要特地指定域名的，但是如果你访问的是金融区的服务，则必须手动指定域名，例如sms的上海金融区域名： sms.ap-shanghai-fsi.aliyuncs.com
  * Usually you don't need to specify the domain name, but if you are accessing a financial area service, you must manually specify the domain name, such as the SMS Shanghai financial area domain name: sms.ap-shanghai-fsi.aliyuncs.com
  *
  * 无特殊需求请不要设置
  */
  private String endpoint;

  /**
  * 区域ID
  * Region ID
  *
  * 用于指定阿里云短信服务的地域。
  * Used to specify the territory of Alibaba Cloud SMS service.
  */
  private String region;

  /**
  * 访问密钥ID
  * Access Key ID
  *
  * 用于身份验证的访问密钥ID。
  * Access Key ID used for authentication.
  *
  * AccessKey查询: https://usercenter.console.aliyun.com/#/manage/ak
  */
  private String accessKeyId;

  /**
  * 访问密钥
  * Access Key
  *
  * 用于身份验证的访问密钥。
  * Access Key used for authentication.
  *
  * AccessKey查询: https://usercenter.console.aliyun.com/#/manage/ak
  */
  private String accessKeySecret;

  /**
   * 应用ID
   * App ID
   *
   * 用于标识腾讯云短信服务的应用ID。
   * App ID used to identify the Tencent Cloud SMS service.
   */
  @Nonnull
  private String appId;

  /**
   * 发送者ID
   * Sender ID
   *
   * 用于标识发送者的ID。
   * Used to identify the sender's ID.
   */
  @Nullable
  private String senderId;

  /**
   * 默认签名
   * Default Sign Name
   *
   * 用于标识短信签名。
   * Used to identify the SMS signature.
   */
  @Nullable
  private String signName;

  /**
   * 短信码号扩展号
   * SMS code number extension number
   *
   * 用于标识短信码号扩展号。
   * Used to identify the SMS code number extension number.
   *
   * 无特殊需求请不要设置
   */
  @Nullable
  private String extendCode;
}
