package org.dinospring.core.sys.token;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Builder.Default
  private String authHeaderName = "D-auth-token";

  @Schema(description = "认证信息的内容")
  public String getAuthPayload() {
    return princ + ';' + token;
  }

  public static String extractPrinc(String authToken) {
    return StringUtils.substringBefore(authToken, ';');
  }

  public static String extractToken(String authToken) {
    return StringUtils.substringAfter(authToken, ';');
  }

}
