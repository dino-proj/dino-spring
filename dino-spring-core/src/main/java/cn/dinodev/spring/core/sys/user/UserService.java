// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.sys.UserType;

/**
 *
 * @author Cody Lu
 */

public interface UserService<T extends User<K>, K extends Serializable> {

  /**
   * 根据用户类型和用户ID获取用户信息
   * @param userType 用户类型
   * @param userId 用户ID
   * @return
   */
  Optional<T> getUserById(UserType userType, String userId);

  /**
   * 用户是否是超级用户
   * @param userType 用户类型
   * @param userId 用户ID
   * @return
   */
  boolean isSuperAdmin(UserType userType, String userId);

  /**
   * 获取用户所有角色
   * @param userType 用户类型
   * @param userId 用户ID
   * @return
   */
  Collection<String> getRoles(UserType userType, String userId);

  /**
   * 获取用户所有权限
   * @param userType 用户类型
   * @param userId 用户ID
   * @return
   */
  Collection<String> getPermissions(UserType userType, String userId);

  /**
   * 用户登录后的回调
   * @param userType 用户类型
   * @param userId 用户ID
   */
  void onLogin(UserType userType, String userId);

}
