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

package org.dinospring.data.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author tuuboo
 */

@NoRepositoryBean
public interface CrudRepositoryBase<T, K> extends JpaRepository<T, K>, JdbcSelectExecutor<T, K> {
  /**
   * 对查询结果进行处理，自动注入TenantId
   * @param entity
   * @return
   */
  default T postQuery(T entity) {
    return entity;
  }

  /**
   * 对查询结果进行处理，自动注入TenantId
   * @param entities
   * @return
   */
  default List<T> postQuery(List<T> entities) {
    if (CollectionUtils.isNotEmpty(entities)) {
      entities.forEach(CrudRepositoryBase.this::postQuery);
    }
    return entities;
  }

  /**
   * 状态设置
   * @param ids
   * @param status
   */
  @Modifying
  @Transactional(rollbackOn = Exception.class)
  @Query("UPDATE #{#entityName} e SET e.status=:status WHERE e.id in :ids")
  void updateStatusByIds(@Param("ids") Collection<K> ids, @Param("status") String status);

  /**
   * 状态设置
   * @param id
   * @param status
   */
  @Modifying
  @Transactional(rollbackOn = Exception.class)
  @Query("UPDATE #{#entityName} e SET e.status=:status WHERE e.id = :id")
  void updateStatusById(@Param("id") K id, @Param("status") String status);

  /**
   * 查询状态为‘ok’的记录数量
   * @return
   */
  @Query("SELECT count(1) AS cnt FROM #{#entityName} e WHERE e.status='ok'")
  Optional<Long> countOk();
}
