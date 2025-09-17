// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss.config;

import jakarta.annotation.Nonnull;

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
public class LocalOssProperties {

  /**
   * 本地存储文件夹路径
   */
  @Nonnull
  private String baseDir;
}