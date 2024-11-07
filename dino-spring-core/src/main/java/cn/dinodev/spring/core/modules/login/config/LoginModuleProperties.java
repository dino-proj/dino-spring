// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.login.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
@Configuration
@ConfigurationProperties(prefix = LoginModuleProperties.PREFIX)
public class LoginModuleProperties {
  public static final String PREFIX = "dino.spring.module.login";

  /**
   * 是否允许多人登录
   */
  private boolean allowMutiDeviceLogin = true;

  /**
   * Mock数据配置
   */
  @NestedConfigurationProperty
  private MockProperties mock;

  /**
   * 登录Token相关配置
   */
  @NestedConfigurationProperty
  private TokenProperties token;

  @Data
  public static class MockProperties {
    /**
     * 允许登录的手机号, 正式环境请不要配置
     */
    private List<String> mobiles;

    /**
     * 允许登录的短信密码，正式环境请不要配置
     */
    private String captcha;
  }

  @Data
  public static class TokenProperties {

    /**
     * 登录token的有效时长，默认2小时
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration loginTokenExpiresIn = Duration.ofHours(2L);

    /**
     * refresh token的有效时长，默认365天
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration refreshTokenExpiresIn = Duration.ofDays(365L);

    /**
     * 签名token的有效时长，默认5分钟
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration signTokenExpiresIn = Duration.ofMinutes(5L);

  }
}
