// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss.config;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TencentCosProperties {
  /**
   * SECRETID和SECRETKEY请登录访问管理控制台进行查看和管理
   */
  @Nonnull
  private String secretId;

  /**
   * SECRETID和SECRETKEY请登录访问管理控制台进行查看和管理
   */
  @Nonnull
  private String secretKey;

  /**
   * bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
   */
  @Nonnull
  private String region;

  /**
   * 代理信息，格式：[ip]:[port]，例如： 192.168.1.1:8080
   */
  @Nullable
  private String httpProxy;
}