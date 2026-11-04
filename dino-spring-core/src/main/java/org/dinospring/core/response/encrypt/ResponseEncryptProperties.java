package org.dinospring.core.response.encrypt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 响应数据加密配置
 * 
 * @author Cody Lu
 * @date 2025-09-17 18:18:02
 */

@Data
@ConfigurationProperties(prefix = ResponseEncryptProperties.PREFIX)
public class ResponseEncryptProperties {
  public static final String PREFIX = "dinospring.response.encrypt";

  /**
   * 是否启用响应数据加密功能
   */
  private boolean enabled = false;

  /**
   * 用于加密的secretKey, 长度必须为16, 24, 或32个字符
   */
  private String aesKey;
}
