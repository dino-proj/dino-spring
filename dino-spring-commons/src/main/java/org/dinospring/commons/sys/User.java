// Copyright 2021 dinospring.cn
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

public interface User<K extends Serializable> extends Serializable {

  @Schema(description = "用户的ID")
  K getId();

  @Schema(description = "用户设备ID")
  String getGuid();

  @Schema(description = "用户类型")
  UserType getUserType();

  @Schema(description = "用户所属租户ID")
  String getTenantId();

  @Schema(description = "用户登录ID")
  String getLoginName();

  @Schema(description = "用户头像 url")
  String getAvatarUrl();

  @Schema(description = "用户显示名")
  String getDisplayName();

  @Schema(description = "用户的SecretKey", hidden = true)
  @JsonIgnore
  String getSecretKey();

  @Schema(description = "用户的状态")
  Integer getStatus();
}
