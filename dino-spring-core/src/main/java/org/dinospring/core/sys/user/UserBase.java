// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.dinospring.commons.sys.User;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
 */
public interface UserBase<K extends Serializable> extends User<K> {

  /**
   * 用户昵称
   * @return
   */
  @Schema(description = "用户昵称")
  String getNickName();

  /**
   * 用户真实姓名
   * @return
   */
  @Schema(description = "用户真实姓名")
  String getRealName();

  /**
   * 用户手机号
   * @return
   */
  @Schema(description = "用户手机号")
  String getMobile();

  /**
   * 用户最后登录时间
   * @return
   */
  @Schema(description = "用户最后登录时间")
  Date getLastLoginAt();

  /**
   * 用户显示名，如果有昵称则显示昵称，没有则显示真实名字
   * @return
   */
  @Schema(description = "用户显示名，如果有昵称则显示昵称，没有则显示真实名字")
  @Override
  default String getDisplayName() {
    return Objects.toString(this.getNickName(), this.getRealName());
  }
}
