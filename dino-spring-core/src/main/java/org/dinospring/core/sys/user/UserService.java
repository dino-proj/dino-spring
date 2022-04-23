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

package org.dinospring.core.sys.user;

import java.io.Serializable;
import java.util.Optional;

import org.dinospring.commons.sys.User;
import org.dinospring.commons.sys.UserType;

/**
 *
 * @author tuuboo
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
   * 用户登录后的回调
   * @param userType 用户类型
   * @param userId 用户ID
   */
  void onLogin(UserType userType, String userId);

}
