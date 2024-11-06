// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package org.dinospring.auth;

import java.util.Objects;

import org.dinospring.auth.session.AuthSession;
import org.dinospring.commons.utils.InheritableThreadLocalMap;

import lombok.experimental.UtilityClass;

/**
 * 权限相关的工具类
 * @author Cody Lu
 * @date 2022-04-08 17:32:22
 */
@UtilityClass
public class DinoAuth {

  private static final InheritableThreadLocalMap RESOURCES = new InheritableThreadLocalMap();
  private static final String AUTH_SESSION_KEY = "AUTH_SESSION";

  /**
   * 获取登录会话
   * @return 登录会话,如果未登录则返回null
   */
  public static <T extends AuthSession> T getAuthSession() {
    return RESOURCES.get(AUTH_SESSION_KEY);
  }

  /**
   * 设置登录会话
   * @param authSession
   */
  public static <T extends AuthSession> void setAuthSession(T authSession) {
    RESOURCES.put(AUTH_SESSION_KEY, authSession);
  }

  /**
   * 是否登录
   * @return
   */
  public static boolean isLogin() {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.isLogin();
    }
    return false;
  }

  /**
   * 当前登录是否是指定类型
   * @return
   */
  public static boolean isLoginAs(String userType) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.isLoginAs(userType);
    }
    return false;
  }

  /**
   * 当前登录用户是否拥有指定权限
   * @return true:拥有指定权限，false:未登录或未拥有指定权限
   */
  public static boolean hasPermission(String permission) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasPermission(permission);
    }
    return false;
  }

  /**
  * 当前登录用户是否拥有指定权限
  * @return true:拥有指定权限，false:未登录或未拥有指定权限
  */
  public static boolean hasPermission(Permission permission) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasPermission(permission);
    }
    return false;
  }

  /**
  * 当前登录用户是否拥有指定权限中的任意一个
  * @return true:拥有指定权限，false:未登录或未拥有指定权限
  */
  public static boolean hasAnyPermission(String... permissions) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasAnyPermission(permissions);
    }
    return false;
  }

  /**
  * 当前登录用户是否拥有指定权限中的任意一个
  * @return true:拥有指定权限，false:未登录或未拥有指定权限
  */
  public static boolean hasAnyPermission(Permission... permissions) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasAnyPermission(permissions);
    }
    return false;
  }

  /**
   * 当前登录用户是否拥有指定权限中的所有
   * @return true:拥有指定权限，false:未登录或未拥有指定权限
   */
  public static boolean hasAllPermission(String... permissions) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasAllPermission(permissions);
    }
    return false;
  }

  /**
  * 当前登录用户是否拥有指定权限中的所有
  * @return true:拥有指定权限，false:未登录或未拥有指定权限
  */
  public static boolean hasAllPermission(Permission... permissions) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasAllPermission(permissions);
    }
    return false;
  }

  /**
   * 当前登录用户是否拥有指定角色
   * @return true:拥有指定角色，false:未登录或未拥有指定角色
   */
  public static boolean hasRole(String role) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasRole(role);
    }
    return false;
  }

  /**
   * 当前登录用户是否拥有指定角色中的任意一个
   * @return true:拥有指定角色，false:未登录或未拥有指定角色
   */
  public static boolean hasAnyRole(String... roles) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasAnyRole(roles);
    }
    return false;
  }

  /**
   * 当前登录用户是否拥有指定角色中的所有
   * @return true:拥有指定角色，false:未登录或未拥有指定角色
   */
  public static boolean hasAllRole(String... roles) {
    var authSession = getAuthSession();
    if (Objects.nonNull(authSession)) {
      return authSession.hasAllRole(roles);
    }
    return false;
  }
}
