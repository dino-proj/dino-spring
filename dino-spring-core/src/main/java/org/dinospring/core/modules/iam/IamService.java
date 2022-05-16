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

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.data.sql.builder.SelectSqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tuuboo
 */
@Service
public class IamService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private ActionGroupRepository actionGroupRepository;

  public List<ActionGroupVo> getAllActionGroups(String userType) {
    if (Objects.isNull(userType)) {
      return actionGroupRepository.findAll(ActionGroupVo.class);
    }
    SelectSqlBuilder selectSqlBuilder = actionGroupRepository.newSelect().isNotNull("user_type").eq("user_type", userType);
    return actionGroupRepository.queryList(selectSqlBuilder, ActionGroupVo.class);
  }

  public List<String> getUserPermissions(String tenantId, String userType, String userId) {
    var roles = userRoleRepository.getUserRoles(tenantId, userType, userId);
    var roleEntities = roleRepository.findAllById(roles);
    return roleEntities.stream().map(RoleEntity::getPermissions).flatMap(List<String>::stream)
      .collect(Collectors.toList());
  }

  public List<String> getUserRoles(String tenantId, String userType, String userId) {
    var roles = userRoleRepository.getUserRoles(tenantId, userType, userId);
    var roleEntities = roleRepository.findAllById(roles);
    return roleEntities.stream().map(RoleEntity::getCode).collect(Collectors.toList());
  }

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

  public long grantRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    return userRoleRepository.addUserRoles(tenantId, userType, userId, roleIds).orElse(0L);
  }

  public long revokeRoles(String tenantId, String userType, String userId, List<Long> roleIds) {
    return userRoleRepository.removeUserRoles(tenantId, userType, userId, roleIds).orElse(0L);
  }

  public long revokeUserRoles(String tenantId, String userType, String userId) {
    return revokeRoles(tenantId, userType, userId, List.of());
  }

}
