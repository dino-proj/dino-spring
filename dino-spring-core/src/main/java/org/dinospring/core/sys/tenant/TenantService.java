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

package org.dinospring.core.sys.tenant;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.core.service.Service;

/**
 *
 * @author tuuboo
 */

public interface TenantService extends Service<TenantEntity, String> {

  TenantRepository tenantRepository();

  /**
   * 获取租户信息
   * @param tenantId
   * @return
   */
  default Tenant findTenantById(String tenantId) {
    return projection(Tenant.class, getById(tenantId));
  }

  /**
   * 根据域名查询Domain
   * @param <T>
   * @param domain
   * @param cls
   * @return
   */
  default <T> T findTenantByDomain(String domain, Class<T> cls) {
    return projection(cls, tenantRepository().getByDomain(domain));
  }
}
