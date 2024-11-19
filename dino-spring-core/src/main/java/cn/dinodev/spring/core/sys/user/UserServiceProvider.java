// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.user;

import java.io.Serializable;

import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.sys.UserType;

/**
 *
 * @author Cody Lu
 */

public interface UserServiceProvider {

  /**
   * 获取 {@link #UserService}
   * @param <T>
   * @param <K>
   * @param userType 用户类型
   * @return
   */
  <T extends User<K>, K extends Serializable> UserService<T, K> resolveUserService(UserType userType);

  /**
   * 获取用户类型
   * @param userType 用户类型字符串
   * @return
   */
  UserType resolveUserType(String userType);

}