package org.dinospring.core.sys.tenant;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.core.service.Service;

public interface TenantService extends Service<TenantEntity, String> {

  default Tenant findTenantById(String tenantId) {
    return projection(Tenant.class, getById(tenantId));
  }
}
