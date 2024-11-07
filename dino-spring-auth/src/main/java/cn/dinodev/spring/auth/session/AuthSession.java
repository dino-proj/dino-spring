// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

// Copyright 2022 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");

package cn.dinodev.spring.auth.session;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import cn.dinodev.spring.auth.Permission;
import cn.dinodev.spring.auth.support.WildcardPermission;

/**
 * 登录会话接口
 * @author Cody Lu
 * @date 2022-04-07 02:16:22
 */

public interface AuthSession {

  /**
   * 获取会话ID
   * @return
   */
  String getSessionId();

  /**
   * 用户是否已登录
   * @return true:已登录，false:未登录
   */
  boolean isLogin();

  /**
   * 用户是否以某种用户类型登录
   * @param subjectType
   * @return true:登录且用户类型匹配，false:未登录或用户类型不匹配
   */
  boolean isLoginAs(String subjectType);

  /**
   * 获取登录用户的ID
   * @return 登录用户的ID
   */
  String getSubjectId();

  /**
   * 获取用户登录类型
   * @return 用户登录类型，如果未登录，返回null
   */
  String getSubjectType();

  /**
   * 获取用户角色
   * @return 用户角色列表
   */
  Collection<String> getSubjectRoles();

  /**
   * 获取用户权限
   * @return 用户权限列表
   */
  Collection<Permission> getSubjectPermissions();

  /**
   * 用户是否有某种权限
   * @param permission 权限字符串
   * @return true:有权限，false:无权限
   */
  default boolean hasPermission(String permission) {
    return this.hasPermission(WildcardPermission.of(permission));
  }

  /**
   * 用户是否有某种权限
   * @param permission 权限对象
   * @return true:有权限，false:无权限
   */
  default boolean hasPermission(Permission permission) {
    var userPerms = this.getSubjectPermissions();
    if (Objects.isNull(userPerms)) {
      return false;
    }
    return userPerms.stream().anyMatch(t -> t.implies(permission));
  }

  /**
   * 用户是否有其中一种权限
   * @param permissions 权限字符串
   * @return true:有权限，false:无权限
   */
  default boolean hasAnyPermission(String... permissions) {
    return this.hasAnyPermission(Arrays.stream(permissions).map(WildcardPermission::of).toArray(Permission[]::new));
  }

  /**
   * 用户是否有其中一种权限
   * @param permissions 权限对象
   * @return true:有权限，false:无权限
   */
  default boolean hasAnyPermission(Permission... permissions) {
    var userPerms = this.getSubjectPermissions();
    if (Objects.isNull(userPerms)) {
      return false;
    }
    for (var permission : permissions) {
      if (userPerms.stream().anyMatch(t -> t.implies(permission))) {
        return true;
      }
    }
    return false;
  }

  /**
   * 用户是否有所有权限
   * @param permissions 权限字符串
   * @return true:有权限，false:无权限
   */
  default boolean hasAllPermission(String... permissions) {
    return this.hasAllPermission(Arrays.stream(permissions).map(WildcardPermission::of).toArray(Permission[]::new));
  }

  /**
   * 用户是否有所有权限
   * @param permissions 权限对象
   * @return true:有权限，false:无权限
   */
  default boolean hasAllPermission(Permission... permissions) {
    var userPerms = this.getSubjectPermissions();
    if (Objects.isNull(userPerms)) {
      return false;
    }
    for (var permission : permissions) {
      if (!userPerms.stream().anyMatch(t -> t.implies(permission))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 用户是否有某种角色
   * @param role 角色字符串
   * @return true:有角色，false:无角色
   */
  default boolean hasRole(String role) {
    var userRoles = this.getSubjectRoles();
    if (Objects.isNull(userRoles)) {
      return false;
    }
    return userRoles.contains(role);
  }

  /**
   * 用户是否有其中一种角色
   * @param roles 角色字符串
   * @return true:有角色，false:无角色
   */
  default boolean hasAnyRole(String... roles) {
    var userRoles = this.getSubjectRoles();
    if (Objects.isNull(userRoles)) {
      return false;
    }
    for (String role : roles) {
      if (userRoles.contains(role)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 用户是否有所有角色
   * @param roles 角色字符串
   * @return true:有角色，false:无角色
   */
  default boolean hasAllRole(String... roles) {
    var userRoles = this.getSubjectRoles();
    if (Objects.isNull(userRoles)) {
      return false;
    }
    for (String role : roles) {
      if (!userRoles.contains(role)) {
        return false;
      }
    }
    return true;
  }

}
