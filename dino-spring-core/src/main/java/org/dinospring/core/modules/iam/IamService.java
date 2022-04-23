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

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class IamService {

  public List<ActionGroupVo> getAllActionGroups(String tenantId) {
    return null;
  }

  public List<ActionGroupVo> getTenantActionGroups(String tenantId) {
    return null;
  }

  public List<String> getUserPermissions(String tenantId, String userType, String userId) {
    return null;
  }

  public <K extends Serializable> List<RoleVo<K>> getUserRoles(String tenantId, String userType, String userId) {
    return null;
  }

  public <K extends Serializable> List<RoleVo<K>> getTenantRoles(String tenantId) {
    return null;
  }

  public RoleEntity saveRole(RoleEntity role) {
    return null;
  }

  public boolean saveRoleUser(List<UserRoleEntity> roleUsers) {
    return false;
  }

}
