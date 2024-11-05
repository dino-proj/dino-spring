// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cody LU
 */

@NoRepositoryBean
public interface CrudRepositoryBase<T, K extends Serializable>
    extends ListCrudRepository<T, K>, JdbcSelectExecutor<T, K>, PagingAndSortingRepository<T, K> {

  /**
   * 根据id，查询元素
   * @param id
   * @return entity or null
   */
  default T getById(K id) {
    return findById(id).orElse(null);
  }

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
   * @param ids 主键id集合
   * @param status 状态
   * @return 更新记录数
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE #{#entityName} e SET e.status=:status WHERE e.id in :ids")
  int updateStatusByIds(@Param("ids") Collection<K> ids, @Param("status") String status);

  /**
   * 状态设置
   * @param id 主键id
   * @param status 状态
   * @return 更新记录数
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE #{#entityName} e SET e.status=:status WHERE e.id = :id")
  int updateStatusById(@Param("id") K id, @Param("status") String status);

  /**
   * 查询状态为‘ok’的记录数量
   * @return
   */
  @Query("SELECT count(1) AS cnt FROM #{#entityName} e WHERE e.status='ok'")
  int countOk();
}
