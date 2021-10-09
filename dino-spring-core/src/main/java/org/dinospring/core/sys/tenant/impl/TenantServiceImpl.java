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

package org.dinospring.core.sys.tenant.impl;

import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.core.sys.tenant.TenantEntity;
import org.dinospring.core.sys.tenant.TenantRepository;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.data.dao.CurdRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuuboo
 */

@Service("tenantService")
public class TenantServiceImpl extends ServiceBase<TenantEntity, String> implements TenantService {
  @Autowired
  private TenantRepository tenantRepository;

  @Override
  public CurdRepositoryBase<TenantEntity, String> repository() {
    return tenantRepository;
  }

}
