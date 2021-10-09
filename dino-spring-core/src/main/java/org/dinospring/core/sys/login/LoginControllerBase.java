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

package org.dinospring.core.sys.login;

import java.io.Serializable;
import org.dinospring.commons.sys.User;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.core.sys.user.UserEntityBase;
import org.slf4j.Logger;

/**
 *
 * @author tuuboo
 */

public interface LoginControllerBase<U extends UserEntityBase<K>, V extends User<K>, K extends Serializable> {

  /**
   * Logger
   * @return
   */
  Logger log();

  /**
   * 租户Service
   * @return
   */
  TenantService tenantService();

  /**
   * 登录Service
   * @return
   */
  LoginServiceBase<U, V, K> loginService();
}
