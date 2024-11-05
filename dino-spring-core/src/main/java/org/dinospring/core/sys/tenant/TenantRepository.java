// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
