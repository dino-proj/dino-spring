// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
@Configuration
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {
  public static final String PREFIX = "dinospring.security";

  /**
   * 是否启用安全机制
   */
  private boolean enabled = true;

  /**
   * 白名单，不需要检查权限的URL列表
   */
  private List<String> whiteList = new ArrayList<>();

  /**
   * 传递签名的HTTP头名字， 默认为"D-auth-token"
   */
  private String authHeaderName = "D-auth-token";
}
