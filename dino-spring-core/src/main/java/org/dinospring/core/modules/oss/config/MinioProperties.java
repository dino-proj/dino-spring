// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.oss.config;

import jakarta.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody LU
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioProperties {
  /**
   * 对象存储服务的URI
   */
  @Nonnull
  private String uri;

  /**
   * Access key账户
   */
  @Nonnull
  private String accessKey;

  /**
   * Secret key秘钥
   */
  @Nonnull
  private String secretKey;
}
