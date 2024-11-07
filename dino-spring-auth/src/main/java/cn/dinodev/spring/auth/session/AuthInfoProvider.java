// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.session;

import java.util.Collection;

import cn.dinodev.spring.auth.Permission;

/**
 * 用户认证信息提供者
 * @author Cody Lu
 * @date 2022-04-09 22:22:42
 */

public interface AuthInfoProvider<S> {

  /**
   * 获取用户角色
   * @param subject 用户
   * @return 用户角色列表
   */
  Collection<String> getRoles(S subject);

  /**
   * 获取用户权限
   * @param subject 用户
   * @return 用户权限列表
   */
  Collection<Permission> getPermissions(S subject);

}
