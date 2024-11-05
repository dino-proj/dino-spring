// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.appclient;

import java.util.List;

import org.dinospring.core.sys.tenant.TenantEntity;
import org.dinospring.data.dao.CrudRepositoryBase;

/**
 * @author Cody LU
 */

public interface AppClientRepository extends CrudRepositoryBase<AppClientEntity, String> {

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
