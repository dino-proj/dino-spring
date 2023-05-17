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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册请求
 * @author tuuboo
 * @date 2022-04-13 03:23:10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegReq extends OpenIMRequest {
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

  /**
   * 用户头像或者群头像url，根据上下文理解
   */
  @JsonProperty("faceURL")
  private String faceURL;

  /**
   * 用户性别(1 表示男，2 表示女)
   */
  @JsonProperty("gender")
  private int gender;

  /**
   * 用户手机号码，包括地区，(如香港：+852-xxxxxxxx)
   */
  @JsonProperty("phoneNumber")
  private String phoneNumber;

  /**
   * 用户生日，Unix时间戳（秒）
   */
  @JsonProperty("birth")
  private String birth;

  /**
   * 邮箱地址
   */
  @JsonProperty("email")
  private String email;

  /**
   * 扩展字段，用户可自行扩展，建议封装成 JSON 字符串
   */
  @JsonProperty("ex")
  private String ex;
}