// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.scope;

import java.util.Optional;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody LU
 * @date 2022-03-30 17:14:41
 */

public interface ScopeRepository extends CrudRepositoryBase<ScopeEntity, Long> {

  /**
   * 根据Hash值查询ScopeID
   * @param hash
   * @return
   */
  @Query("SELECT e.id FROM #{#entityName} e WHERE e.ruleHash = :hash")
  Optional<Long> getByRuleHash(String hash);
}
