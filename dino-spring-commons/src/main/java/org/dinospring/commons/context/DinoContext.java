// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.context;

import java.io.Serializable;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;

/**
 *
 * @author Cody Lu
 */
public interface DinoContext {
  /**
   * 获取当前登录的用户
   * @param <K> 用户ID类型参数
   * @return
   */
  <K extends Serializable> User<K> currentUser();

  /**
   * 设置当前用户
   * @param <K>
   * @param user
   */
  <K extends Serializable> void currentUser(User<K> user);

  /**
   * 获取当前租户
   * @return
   */
  Tenant currentTenant();

  /**
   * 设置当前租户信息
   * @param tenant
   */
  void currentTenant(Tenant tenant);
}
