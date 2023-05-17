// Copyright 2021 dinodev.cn
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

import org.dinospring.commons.sys.User;
import org.dinospring.commons.sys.UserType;

/**
 *
 * @author tuuboo
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
