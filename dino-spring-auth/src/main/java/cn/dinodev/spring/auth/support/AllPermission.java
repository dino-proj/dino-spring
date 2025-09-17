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

  public static AllPermission of() {
    return INSTANCE;
  }

}
