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

package org.dinospring.core.sys.tenant;

import java.util.Optional;

import jakarta.annotation.Nonnull;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody LU
 */

public interface TenantRepository extends CrudRepositoryBase<TenantEntity, String> {

  /**
   * 通过域值获取租户信息
   * @param domain
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.customDomain = :domain")
  Optional<TenantEntity> getByDomain(@Nonnull String domain);

  /**
   * 通过code获取租户信息
   * @param code
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.code = :code")
  Optional<TenantEntity> getByCode(@Nonnull String code);
}
