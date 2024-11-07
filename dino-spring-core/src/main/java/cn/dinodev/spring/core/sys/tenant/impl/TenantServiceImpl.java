// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.tenant.impl;

import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.core.sys.tenant.TenantEntity;
import cn.dinodev.spring.core.sys.tenant.TenantRepository;
import cn.dinodev.spring.core.sys.tenant.TenantService;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cody Lu
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
