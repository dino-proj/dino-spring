// Copyright 2021 dinodev.cn
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

package org.dinospring.core.modules.login.tenanted;

import java.io.Serializable;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.sys.User;
import org.dinospring.core.modules.login.LoginControllerBase;
import org.dinospring.core.sys.tenant.TenantService;

/**
 *
 * @author Cody LU
 */

public interface LoginControllerBaseTenanted<U extends User<K>, K extends Serializable>
    extends LoginControllerBase<U, K> {

  /**
   * 租户Service
   * @return
   */
  default TenantService tenantService() {
    return ContextHelper.findBean(TenantService.class);
  }

}
