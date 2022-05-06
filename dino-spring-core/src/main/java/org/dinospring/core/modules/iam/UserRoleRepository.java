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

package org.dinospring.core.modules.iam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tuuboo
 * @date 2022-05-06 08:30:43
 */

@Repository
public interface UserRoleRepository extends CrudRepositoryBase<UserRoleEntity, Long> {

  /**
   * 添加用户的角色
   * @param tenantId
   * @param userType
   * @param userId
   * @param roleIds
   * @return
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  default Optional<Long> addUserRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    var sql = newSelect();
    sql.eq("tenant_id", tenantId);
    sql.column("id, role_id");
    sql.eq("user_type", userType);
    sql.eq("user_id", userId);
    sql.in("role_id", roleIds);

    var existRoles = this.queryForMap(sql, "role_id", Long.class, "id", Long.class);
    var restRoles = roleIds.stream().filter(roleId -> !existRoles.containsKey(roleId)).collect(Collectors.toList());
    if (!restRoles.isEmpty()) {
      var entities = restRoles.stream()
          .map(roleId -> UserRoleEntity.builder().tenantId(tenantId).userType(userType).userId(userId).roleId(roleId)
              .build())
          .collect(Collectors.toList());
      this.saveAll(entities);
    }

    return Optional.of(Long.valueOf(roleIds.size()));
  }

  /**
   * 移除用户的角色
   * @param tenantId
   * @param userType
   * @param userId
   * @param roleIds
   * @return
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  default Optional<Long> removeUserRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    var sql = newDelete();
    sql.eq("tenant_id", tenantId);
    sql.eq("user_type", userType);
    sql.eq("user_id", userId);
    sql.in("role_id", roleIds);

    return Optional.of(this.delete(sql));
  }

  /**
   * 获取用户的角色id列表，无分页
   * @param tenantId
   * @param userType
   * @param userId
   * @return
   */
  default List<Long> getUserRoles(String tenantId, String userType, String userId) {
    var sql = newSelect();
    sql.column("role_id");
    sql.eq("tenant_id", tenantId);
    sql.eq("user_type", userType);
    sql.eq("user_id", userId);
    return this.queryList(sql, Long.class);
  }

  /**
   * 获取用户的角色id列表，分页
   * @param tenantId
   * @param userType
   * @param userId
   * @param pageable
   * @return
   */
  default Page<Long> listUserRoles(String tenantId, String userType, String userId, Pageable pageable) {
    var sql = newSelect();
    sql.column("role_id");
    sql.eq("tenant_id", tenantId);
    sql.eq("user_type", userType);
    sql.eq("user_id", userId);
    return this.queryPage(sql, pageable, Long.class);
  }

}
