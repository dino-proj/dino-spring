// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.token;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token类，用于表示用户认证令牌信息
 *
 * @author Cody Lu
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

  @Schema(description = "Base64加密的用户信息")
  private String princ;

  @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
  @Schema(description = "Token串")
  private String token;

  @Schema(description = "刷新Token串")
  private String refreshToken;

  @Schema(description = "Token过期时间，单位秒")
  private long expiresIn;

  @Schema(description = "请求时附带的Http Header的名字")
  private String authHeaderName;

  /**
   * 获取认证信息的内容，格式为：princ;accessToken
   * @return 认证负载字符串
   */
  @Schema(description = "认证信息的内容")
  public String getAuthPayload() {
    return this.princ + ';' + this.token;
  }

  /**
   * 从认证Token中提取用户主体信息
   * @param authToken 认证Token字符串
   * @return 用户主体信息
   */
  public static String extractPrinc(String authToken) {
    return StringUtils.substringBefore(authToken, ';');
  }

  /**
   * 从认证Token中提取访问Token
   * @param authToken 认证Token字符串
   * @return 访问Token
   */
  public static String extractToken(String authToken) {
    return StringUtils.substringAfter(authToken, ';');
  }

}
