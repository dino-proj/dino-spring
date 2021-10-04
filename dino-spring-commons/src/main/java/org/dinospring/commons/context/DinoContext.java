// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.commons.context;

import java.io.Serializable;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;

/**
 *
 * @author tuuboo
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
