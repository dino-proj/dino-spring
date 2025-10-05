// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.support;

import cn.dinodev.spring.auth.Permission;

/**
 * 所有权限，总是有权限访问
 * @author Cody Lu
 * @date 2022-04-07 03:26:06
 */

public class AllPermission implements Permission {
  private static final AllPermission INSTANCE = new AllPermission();

  @Override
  public boolean implies(Permission permission) {
    return true;
  }

  /**
   * 获取全权限实例
   *
   * <p>返回单例的全权限对象，该对象对任何权限检查都返回true。
   * 通常用于超级管理员或系统级操作，需要绕过所有权限检查的场景。</p>
   *
   * @return 全权限实例
   */
  public static AllPermission of() {
    return INSTANCE;
  }

}
