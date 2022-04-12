// Copyright 2022 dinospring.cn
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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册请求
 * @author tuuboo
 * @date 2022-04-13 03:23:10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegReq extends Request {
  public static final String PATH = "/auth/user_register";

  /**
   * 要获取token的用户id
   */
  @JsonProperty("userID")
  private String userId;

  /**
   * 用户登录或注册的平台类型，管理员填8
   */
  @JsonProperty("platform")
  private Integer platform;

  /**
   * OpenIM秘钥
   */
  @JsonProperty("secret")
  private String secret;

  /**
   * 用户昵称
   */
  @JsonProperty("nickname")
  private String nickname;
}