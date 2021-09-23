package org.dinospring.core.modules.appclient;

import java.util.List;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppClientService extends ServiceBase<AppClientEntity, String> {

  @Autowired
  private AppClientRepository appClientRepository;

  public List<Tenant> findBindTenants(String appClientId) {
    return projection(Tenant.class, appClientRepository.findBindTenants(appClientId));
  }

  @Override
  public CURDRepositoryBase<AppClientEntity, String> repository() {
    return appClientRepository;
  }

}
