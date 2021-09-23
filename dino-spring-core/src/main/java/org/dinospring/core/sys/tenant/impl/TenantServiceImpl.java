package org.dinospring.core.sys.tenant.impl;

import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.core.sys.tenant.TenantEntity;
import org.dinospring.core.sys.tenant.TenantRepository;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tenantService")
public class TenantServiceImpl extends ServiceBase<TenantEntity, String> implements TenantService {
  @Autowired
  private TenantRepository tenantRepository;

  @Override
  public CURDRepositoryBase<TenantEntity, String> repository() {
    return tenantRepository;
  }

}
