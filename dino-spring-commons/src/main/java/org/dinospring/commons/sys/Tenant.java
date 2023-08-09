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

public interface Tenant extends Serializable {
  /**
   * 代表系统的tenant ID；
   */
  public static final String TENANT_SYS = "_SYS_";

  /**
   * 租户ID
   * @return
   */
  @Schema(description = "租户ID")
  String getId();

  /**
   * 租户名称
   * @return
   */
  @Schema(description = "租户名称")
  String getName();

  /**
   * 租户简称
   * @return
   */
  @Schema(description = "租户简称")
  String getShortName();

  /**
   * 租户LOGO
   * @return
   */
  @Schema(description = "租户LOGO")
  String getIconUrl();

  /**
   * 租户自定义域名,如果用户配置了域名，则用其自己的域名
   * @return
   */
  @Schema(description = "租户自定义域名,如果用户配置了域名，则用其自己的域名")
  String getCustomDomain();

  /**
   * 租户的SecretKey
   * @return
   */
  @Schema(description = "租户的SecretKey", hidden = true)
  @JsonIgnore
  String getSecretKey();

  /**
   * 租户状态
   * @return
   */
  @Schema(description = "租户状态")
  String getStatus();

  /**
   * 是否系统租户
   * @param tenantId
   * @return
   */
  static boolean isSys(String tenantId) {
    return TENANT_SYS.equals(tenantId);
  }
}
