// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth;

/**
 * 权限接口
 * @author Cody Lu
 * @date 2022-04-07 03:05:11
 */

public interface Permission {

  /**
  * 判断用户是否有权限访问某个资源
  * @param permission 权限对象
  * @return true:有权限，false:无权限
  */
  boolean implies(Permission permission);
}
