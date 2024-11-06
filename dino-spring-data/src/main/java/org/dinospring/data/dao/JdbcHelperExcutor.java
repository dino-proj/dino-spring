// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.dao;

import java.util.Map;

import org.dinospring.data.sql.builder.DeleteSqlBuilder;
import org.dinospring.data.sql.builder.UpdateSqlBuilder;
import org.springframework.data.jdbc.repository.query.Modifying;

/**
 *
 * @author Cody Lu
 */

public interface JdbcHelperExcutor<T, K> {

  /**
   * entityClass
   * @return
   */
  Class<T> entityClass();

  /**
   * keyClass
   * @return
   */
  Class<K> keyClass();

  /**
   * 获取实体元信息
   * @return entity 元信息
   */
  EntityMeta entityMeta();

  /**
   * 返回表名，并根据{@code TenantTable#TenantLevel}分表策略组合表名
   * @return
   */
  default String tableName() {
    return tableName(entityClass());
  }

  /**
   * 返回指定Entity的表名，并根据{@code TenantTable#TenantLevel}分表策略组合表名
   * @param <C>
   * @param entityClass entity class
   * @return
   */
  <C> String tableName(Class<C> entityClass);

  /**
   * 更新指定列
   * @param id
   * @param column
   * @param value
   * @return
   */
  @Modifying
  default boolean updateById(K id, String column, Object value) {
    return updateById(id, Map.of(column, value));
  }

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param column
   * @param value
   * @param version
   * @return
   */
  @Modifying
  default boolean updateByIdWithVersion(K id, String column, Object value, Number version) {
    return updateByIdWithVersion(id, Map.of(column, value), version);
  }

  /**
   * 更新指定列
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @return
   */
  @Modifying
  default boolean updateById(K id, String column1, Object value1, String column2, Object value2) {
    return updateById(id, Map.of(column1, value1, column2, value2));
  }

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param version
   * @return
   */
  @Modifying
  default boolean updateByIdWithVersion(K id, String column1, Object value1, String column2, Object value2,
      Number version) {
    return updateByIdWithVersion(id, Map.of(column1, value1, column2, value2), version);
  }

  /**
   * 更新指定列
   * @param id
   * @param columnValue
   * @return
   */
  @Modifying
  boolean updateById(K id, Map<String, Object> columnValue);

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param columnValue
   * @param version
   * @return
   */
  @Modifying
  boolean updateByIdWithVersion(K id, Map<String, Object> columnValue, Number version);

  /**
   * 修改
   * @param updateSqlBuilder
   * @return
   */
  @Modifying
  long update(UpdateSqlBuilder updateSqlBuilder);

  /**
   * 删除
   * @param deleteSqlBuilder
   * @return
   */
  @Modifying
  long delete(DeleteSqlBuilder deleteSqlBuilder);
}
