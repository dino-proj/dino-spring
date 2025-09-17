// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.sys;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
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
