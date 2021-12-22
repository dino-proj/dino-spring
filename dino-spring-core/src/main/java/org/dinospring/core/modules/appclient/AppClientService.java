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

import org.dinospring.commons.sys.Tenant;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuuboo
 */

@Service
public class AppClientService extends ServiceBase<AppClientEntity, String> {

  @Autowired
  private AppClientRepository appClientRepository;

  public List<Tenant> findBindTenants(String appClientId) {
    return projection(Tenant.class, appClientRepository.findBindTenants(appClientId));
  }

  @Override
  public CrudRepositoryBase<AppClientEntity, String> repository() {
    return appClientRepository;
  }

}
