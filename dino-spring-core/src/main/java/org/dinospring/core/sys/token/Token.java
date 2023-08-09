// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.sys.token;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody LU
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
