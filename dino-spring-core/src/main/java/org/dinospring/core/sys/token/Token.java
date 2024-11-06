// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.token;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
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

  @Schema(description = "Token串")
  private String token;

  @Schema(description = "刷新Token串")
  private String refreshToken;

  @Schema(description = "Token过期时间，单位秒")
  private long expiresIn;

  @Schema(description = "请求时附带的Http Header的名字")
  private String authHeaderName;

  @Schema(description = "认证信息的内容")
  public String getAuthPayload() {
    return this.princ + ';' + this.token;
  }

  public static String extractPrinc(String authToken) {
    return StringUtils.substringBefore(authToken, ';');
  }

  public static String extractToken(String authToken) {
    return StringUtils.substringAfter(authToken, ';');
  }

}
