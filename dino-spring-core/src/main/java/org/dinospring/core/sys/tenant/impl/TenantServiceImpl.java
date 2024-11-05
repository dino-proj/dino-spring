// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.tenant.impl;

import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.core.sys.tenant.TenantEntity;
import org.dinospring.core.sys.tenant.TenantRepository;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cody LU
 */

@Service("tenantService")
public class TenantServiceImpl extends ServiceBase<TenantEntity, String> implements TenantService {
  @Autowired
  private TenantRepository tenantRepository;

  @Override
  public CrudRepositoryBase<TenantEntity, String> repository() {
    return tenantRepository;
  }

  @Override
  public TenantRepository tenantRepository() {
    return tenantRepository;
  }

}
