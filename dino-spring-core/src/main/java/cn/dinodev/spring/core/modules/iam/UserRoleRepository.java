// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.iam;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;

import cn.dinodev.spring.core.entity.Code;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;

/**
 *
 * @author Cody Lu
 * @date 2022-05-06 08:30:43
 */

public interface UserRoleRepository extends CrudRepositoryBase<UserRoleEntity, Long> {

  String COLUMN_TENANT_ID = "tenant_id";
  String COLUMN_USER_TYPE = "user_type";
  String COLUMN_USER_ID = "user_id";
  String COLUMN_ROLE_ID = "role_id";

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
  default Optional<Integer> addUserRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    var sql = newSelect();
    sql.eq(COLUMN_TENANT_ID, tenantId);
    sql.column("id, role_id");
    sql.eq(COLUMN_USER_TYPE, userType);
    sql.eq(COLUMN_USER_ID, userId);
    sql.in(COLUMN_ROLE_ID, roleIds);

    var existRoles = this.queryForMap(sql, COLUMN_ROLE_ID, Long.class, "id", Long.class);
    var restRoles = roleIds.stream().filter(roleId -> !existRoles.containsKey(roleId)).collect(Collectors.toList());
    if (!restRoles.isEmpty()) {
      var entities = restRoles.stream()
          .map(roleId -> UserRoleEntity.builder()
              .tenantId(tenantId)
              .userType(userType)
              .userId(userId)
              .roleId(roleId)
              .createAt(new Date())
              .status(Code.STATUS.OK.getName())
              .updateAt(new Date())
              .build())
          .collect(Collectors.toList());
      this.saveAll(entities);
    }

    return Optional.of(roleIds.size());
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
  default Optional<Integer> removeUserRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    var sql = newDelete();
    sql.eq(COLUMN_TENANT_ID, tenantId);
    sql.eq(COLUMN_USER_TYPE, userType);
    sql.eq(COLUMN_USER_ID, userId);
    sql.in(COLUMN_ROLE_ID, roleIds);

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
    sql.column(COLUMN_ROLE_ID);
    if (StringUtils.isNoneBlank(tenantId)) {
      sql.eq(COLUMN_TENANT_ID, tenantId);
    }
    sql.eq(COLUMN_USER_TYPE, userType);
    sql.eq(COLUMN_USER_ID, userId);
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
    sql.column(COLUMN_ROLE_ID);
    sql.eq(COLUMN_TENANT_ID, tenantId);
    sql.eq(COLUMN_USER_TYPE, userType);
    sql.eq(COLUMN_USER_ID, userId);
    return this.queryPage(sql, pageable, Long.class);
  }

}
