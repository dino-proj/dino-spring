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

import org.dinospring.commons.sys.UserType;
import org.dinospring.data.dao.CurdRepositoryBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tuuboo
 */

@NoRepositoryBean
public interface UserRepositoryBase<T extends UserEntityBase<K>, K extends Serializable>
    extends CurdRepositoryBase<T, K> {

  /**
   * 根据手机号查询用户
   * @param tenantId
   * @param mobile
   * @param userType
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.mobile=:mobile and e.tenantId=:tenantId and e.userType=:userType")
  Optional<T> findUserByMobile(String tenantId, String mobile, UserType userType);


  /**
   * 根据手机号查询用户
   * @param tenantId
   * @param mobile
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.mobile=:mobile and e.tenantId=:tenantId")
  Optional<T> findUserByMobile(String tenantId, String mobile);

  /**
   * 根据登录用户名查询用户
   * @param tenantId
   * @param username
   * @param userType
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.loginName=:username and e.tenantId=:tenantId and e.userType=:userType")
  Optional<T> findUserByLoginName(String tenantId, String username, UserType userType);

  /**
   * 根据登录用户名查询用户
   * @param tenantId
   * @param username
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.loginName=:username and e.tenantId=:tenantId")
  Optional<T> findUserByLoginName(String tenantId, String username);

  /**
   * 更新用户最后登录时间
   * @param tenantId
   * @param id
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  @Query("UPDATE #{#entityName} e set lastLoginAt = now() WHERE e.id=:id and e.tenantId=:tenantId")
  void updateLastLogin(String tenantId, K id);
}
