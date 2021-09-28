package org.dinospring.core.modules.oss.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Data;

@Data
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