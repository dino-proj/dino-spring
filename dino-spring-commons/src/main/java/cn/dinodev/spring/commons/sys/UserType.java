// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.sys;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户类型接口，定义用户类型的基本属性
 * 
 * @author Cody Lu
 */
public interface UserType extends Serializable {

  /**
   * 获取用户类型名称
   * 
   * @return 用户类型名称
   */
  @Schema(description = "用户类型名称")
  String getType();

  /**
   * 是否为隶属于某个租户下面的用户
   * @return
   */
  boolean isTenantUser();

  /**
   * 所有用户类型
   * @return
   */
  @Schema(description = "所有用户类型", hidden = true)
  List<UserType> allTypes();

}
