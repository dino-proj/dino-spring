// Copyright 2022 dinodev.cn
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
 * 登录会话接口
 * @author tuuboo
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

}
