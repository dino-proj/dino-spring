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

package org.dinospring.core.modules.appclient;

import java.util.List;

import org.dinospring.core.sys.tenant.TenantEntity;
import org.dinospring.data.dao.CurdRepositoryBase;

/**
 * @author tuuboo
 */

public interface AppClientRepository extends CurdRepositoryBase<AppClientEntity, String> {

  /**
   * 查询客户端绑定的租户信息
   * @param appClientId
   * @return
   */
  default List<TenantEntity> findBindTenants(String appClientId) {
    var sql = newSelect(TenantEntity.class, "t");
    sql.column("t.*");
    sql.join("sys_app_client_tenant_rel", "r", "t.id=r.tenant_id");
    sql.eq("r.app_client_id", appClientId);
    return this.queryList(sql, TenantEntity.class);
  }
}
