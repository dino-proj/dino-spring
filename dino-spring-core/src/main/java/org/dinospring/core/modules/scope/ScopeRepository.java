// Copyright 2022 dinospring.cn
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

package org.dinospring.core.modules.scope;

import java.util.Optional;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tuuboo
 * @date 2022-03-30 17:14:41
 */

@Repository
public interface ScopeRepository extends CrudRepositoryBase<ScopeEntity, Long> {

  /**
   * 根据Hash值查询ScopeID
   * @param hash
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.ruleHash = :hash")
  Optional<Long> getByRuleHash(String hash);
}
