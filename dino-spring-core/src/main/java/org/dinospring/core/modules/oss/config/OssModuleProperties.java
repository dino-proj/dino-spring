package org.dinospring.core.modules.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = OssModuleProperties.PREFIX)
public class OssModuleProperties {
  public static final String PREFIX = "dinospring.module.oss";

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

}
