// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
@ConfigurationProperties(prefix = OssModuleProperties.PREFIX)
public class OssModuleProperties {
  public static final String PREFIX = "dino.spring.module.oss";

  /**
   * 配置Minio
   */
  private MinioProperties minio;

  /**
   * 配置本地oss存储
   */
  private LocalOssProperties local;

  /**
   * 配置腾讯云Cos存储
   */
  private TencentCosProperties tencentCos;

  /**
   * oss服务的URLBase
   */
  private String ossUrlBase;

}
