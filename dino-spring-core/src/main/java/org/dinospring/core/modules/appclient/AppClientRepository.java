package org.dinospring.core.modules.appclient;

import java.util.List;

import org.dinospring.core.sys.tenant.TenantEntity;
import org.dinospring.data.dao.CURDRepositoryBase;

public interface AppClientRepository extends CURDRepositoryBase<AppClientEntity, String> {

  default List<TenantEntity> findBindTenants(String appClientId) {
    var sql = newSelect(TenantEntity.class, "t");
    sql.column("t.*");
    sql.join("sys_app_client_tenant_rel", "r", "t.id=r.tenant_id");
    sql.eq("r.app_client_id", appClientId);
    return this.queryList(sql, TenantEntity.class);
  }
}
