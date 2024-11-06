// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.sys;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
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
