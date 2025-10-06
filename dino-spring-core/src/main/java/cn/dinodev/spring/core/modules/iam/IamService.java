// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.iam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

/**
 * @author Cody Lu
 */
@Service
public class IamService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private ActionGroupRepository actionGroupRepository;

  /**
   * 获取所有操作组
   *
   * @param userType 用户类型，如果为 null 则返回所有操作组
   * @return 操作组列表
   */
  public List<ActionGroupVo> getAllActionGroups(String userType) {
    if (Objects.isNull(userType)) {
      return actionGroupRepository.findAll(ActionGroupVo.class);
    }
    SelectSqlBuilder selectSqlBuilder = actionGroupRepository.newSelect()
        .isNotNull("user_type")
        .eq("user_type", userType);
    return actionGroupRepository.queryList(selectSqlBuilder, ActionGroupVo.class);
  }

  /**
   * 获取用户的所有权限
   *
   * @param tenantId 租户 ID
   * @param userType 用户类型
   * @param userId 用户 ID
   * @return 权限列表
   */
  public List<String> getUserPermissions(String tenantId, String userType, String userId) {
    var roles = userRoleRepository.getUserRoles(tenantId, userType, userId);
    var roleEntities = roleRepository.findAllById(roles);
    return roleEntities.stream().map(RoleEntity::getPermissions).flatMap(List<String>::stream)
        .collect(Collectors.toList());
  }

  /**
   * 获取用户的所有角色编码
   *
   * @param tenantId 租户 ID
   * @param userType 用户类型
   * @param userId 用户 ID
   * @return 角色编码列表
   */
  public List<String> getUserRoles(String tenantId, String userType, String userId) {
    var roles = userRoleRepository.getUserRoles(tenantId, userType, userId);
    var roleEntities = roleRepository.findAllById(roles);
    return roleEntities.stream().map(RoleEntity::getCode).collect(Collectors.toList());
  }

  /**
   * 分页获取用户的角色列表
   *
   * @param tenantId 租户 ID
   * @param userType 用户类型
   * @param userId 用户 ID
   * @param pageable 分页参数
   * @return 角色分页结果
   */
  public Page<RoleVo> listUserRoles(String tenantId, String userType, String userId,
      Pageable pageable) {
    var roles = userRoleRepository.listUserRoles(tenantId, userType, userId, pageable);
    var roleIds = roles.getContent();
    if (CollectionUtils.isNotEmpty(roleIds)) {
      var roleVos = roleRepository.findAllById(roleIds, RoleVo.class);
      return new PageImpl<>(roleVos, pageable, roles.getTotalElements());
    }
    return new PageImpl<>(List.of(), pageable, roles.getTotalElements());
  }

  /**
   * 为用户授予角色
   *
   * @param tenantId 租户 ID
   * @param userType 用户类型
   * @param userId 用户 ID
   * @param roleIds 角色 ID 列表
   * @return 授予的角色数量
   */
  public int grantRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    return userRoleRepository.addUserRoles(tenantId, userType, userId, roleIds).orElse(0);
  }

  /**
   * 撤销用户的角色
   *
   * @param tenantId 租户 ID
   * @param userType 用户类型
   * @param userId 用户 ID
   * @param roleIds 角色 ID 列表
   * @return 撤销的角色数量
   */
  public int revokeRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    return userRoleRepository.removeUserRoles(tenantId, userType, userId, roleIds).orElse(0);
  }

  /**
   * 撤销用户的所有角色
   *
   * @param tenantId 租户 ID
   * @param userType 用户类型
   * @param userId 用户 ID
   * @return 撤销的角色数量
   */
  public int revokeUserRoles(String tenantId, String userType, String userId) {
    return revokeRoles(tenantId, userType, userId, List.of());
  }

}
