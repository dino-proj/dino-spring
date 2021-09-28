package org.dinospring.core.modules.oss.config;

import javax.annotation.Nonnull;

import lombok.Data;

@Data
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
