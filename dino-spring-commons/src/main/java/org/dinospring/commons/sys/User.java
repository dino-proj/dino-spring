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

package org.dinospring.commons.sys;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody LU
 * @author JL
 */
public interface User<K extends Serializable> extends Serializable {

  /**
   * 用户的ID
   * @return
   */
  @Schema(description = "用户的ID")
  K getId();

  /**
   * 用户类型
   * @return
   */
  @Schema(description = "用户类型")
  UserType getUserType();

  /**
   * 用户所属租户ID
   * @return
   */
  @Schema(description = "用户所属租户ID")
  String getTenantId();

  /**
   * 用户登录ID
   * @return
   */
  @Schema(description = "用户登录ID")
  String getLoginName();

  /**
   * 用户头像 url
   * @return
   */
  @Schema(description = "用户头像 url")
  String getAvatarUrl();

  /**
   * 用户显示名
   * @return
   */
  @Schema(description = "用户显示名")
  String getDisplayName();

  /**
  * 用户的密码Hash，@JsonIgnore，对外不可见
  * @return
  */
  @Schema(description = "用户的密码Hash", hidden = true)
  @JsonIgnore
  String getPasswordHash();

  /**
   * 用户的SecretKey，@JsonIgnore，对外不可见
   * @return
   */
  @Schema(description = "用户的SecretKey", hidden = true)
  @JsonIgnore
  String getSecretKey();

  /**
   * 用户的状态
   * @return
   */
  @Schema(description = "用户的状态")
  String getStatus();
}
