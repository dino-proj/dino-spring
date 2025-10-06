// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.user;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.dinodev.spring.commons.sys.UserType;

/**
 *
 * @author Cody Lu
 */

public enum DefaultUserType implements UserType {
  //系统用户
  SYS("sys", false),
  //系统API接口用户
  SYS_API("sysapi", false),
  //后台管理用户
  ADMIN("admin", true),
  //前端用户
  CLIENT("client", true),
  //租户api接口用户
  API("api", true),
  //访客用户
  GUEST("guest", true);

  private final String userType;

  private final boolean tenantUser;

  DefaultUserType(String userType, boolean tenantUser) {
    this.userType = userType;
    this.tenantUser = tenantUser;
  }

  @Override
  public String getType() {
    return userType;
  }

  @Override
  public boolean isTenantUser() {
    return tenantUser;
  }

  @Override
  public String toString() {
    return this.getType();
  }

  @Override
  public List<UserType> allTypes() {
    return Arrays.stream(DefaultUserType.values()).collect(Collectors.toList());
  }

  /**
   * 根据用户类型字符串获取对应的枚举值
   * @param userType 用户类型字符串
   * @return 对应的UserType枚举值
   */
  public static UserType of(String userType) {
    return DefaultUserType.valueOf(userType.toUpperCase());
  }
}
