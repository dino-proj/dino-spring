// Copyright 2022 dinodev.cn
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

package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户token信息
 * @author tuuboo
 * @date 2022-04-13 04:15:34
 */

@Data
public class UserToken {

  @Schema(description = "用户ID")
  private String userId;

  @Schema(description = "登录后的Token信息")
  private String token;

  @Schema(description = "token过期时间戳（秒）")
  private Long expiredTime;

  @JsonProperty("userID")
  public void setUserId(String userId) {
    this.userId = userId;
  }

  @JsonProperty("token")
  public void setToken(String token) {
    this.token = token;
  }

  @JsonProperty("expiredTime")
  public void setExpiredTime(Long expiredTime) {
    this.expiredTime = expiredTime;
  }
}
