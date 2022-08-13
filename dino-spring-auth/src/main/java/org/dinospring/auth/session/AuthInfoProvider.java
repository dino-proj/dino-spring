// Copyright 2022 dinospring.cn
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

package org.dinospring.auth.session;

import java.util.Collection;

import org.dinospring.auth.Permission;

/**
 * 用户认证信息提供者
 * @author tuuboo
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
