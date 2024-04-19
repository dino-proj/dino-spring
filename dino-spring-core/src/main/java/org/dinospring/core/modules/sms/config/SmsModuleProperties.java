// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.sms.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author Cody LU
 */

@Data
@Configuration
@ConfigurationProperties(prefix = SmsModuleProperties.PREFIX)
public class SmsModuleProperties {
  public static final String PREFIX = "dinospring.module.sms";

  /**
   * 短信厂商
   */
  private SmsVendor vendor;

  /**
   * 验证码配置
   */
  @NestedConfigurationProperty
  private CaptchaProperties captcha;

  /**
   * 阿里云短信配置
   */
  @NestedConfigurationProperty
  private AliSmsProperties ali;

  /**
   * 腾讯云短信配置
   */
  @NestedConfigurationProperty
  private TencentSmsProperties tencent;

  @Data
  public static class CaptchaProperties {
    /**
     * 验证码长度, 默认6位
     */
    private int length = 6;

    /**
     * 验证码有效期，默认5分钟
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration expire = Duration.ofMinutes(5);

    /**
     * 验证码模版ID
     */
    private String templateId;
  }

  public static enum SmsVendor {
    /**
     * 阿里巴巴短信通道
     */
    ALI,
    /**
     * 腾讯短信通道
     */
    TENCENT;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }
}
