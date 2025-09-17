// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.dao;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;

import cn.dinodev.spring.data.sql.builder.DeleteSqlBuilder;
import cn.dinodev.spring.data.sql.builder.UpdateSqlBuilder;
import jakarta.annotation.Nullable;

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
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateById(K id, String column, Object value) {
    return updateById(id, Map.of(column, value));
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
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateById(K id, String column1, Object value1, String column2, Object value2) {
    return updateById(id, Map.of(column1, value1, column2, value2));
  }

  /**
   * 更新指定列
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param column3
   * @param value3
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateById(K id, String column1, Object value1, String column2, Object value2, String column3,
      Object value3) {
    return updateById(id, Map.of(column1, value1, column2, value2, column3, value3));
  }

  /**
   * 更新指定列
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param column3
   * @param value3
   * @param column4
   * @param value4
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateById(K id, String column1, Object value1, String column2, Object value2, String column3,
      Object value3, String column4, Object value4) {
    return updateById(id, Map.of(column1, value1, column2, value2, column3, value3, column4, value4));
  }

  /**
   * 更新指定列
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param column3
   * @param value3
   * @param column4
   * @param value4
   * @param column5
   * @param value5
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateById(K id, String column1, Object value1, String column2, Object value2, String column3,
      Object value3, String column4, Object value4, String column5, Object value5) {
    return updateById(id, Map.of(column1, value1, column2, value2, column3, value3, column4, value4, column5, value5));
  }

  /**
   * 更新指定列
   * @param id
   * @param columnValue
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  boolean updateById(K id, Map<String, Object> columnValue);

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param column
   * @param value
   * @param version
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateByIdWithVersion(K id, String column, Object value, Number version) {
    return updateByIdWithVersion(id, Map.of(column, value), version);
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
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateByIdWithVersion(K id, String column1, Object value1, String column2, Object value2,
      Number version) {
    return updateByIdWithVersion(id, Map.of(column1, value1, column2, value2), version);
  }

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param column3
   * @param value3
   * @param version
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateByIdWithVersion(K id, String column1, Object value1, String column2, Object value2,
      String column3, Object value3, Number version) {
    return updateByIdWithVersion(id, Map.of(column1, value1, column2, value2, column3, value3), version);
  }

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param column3
   * @param value3
   * @param column4
   * @param value4
   * @param version
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateByIdWithVersion(K id, String column1, Object value1, String column2, Object value2,
      String column3, Object value3, String column4, Object value4, Number version) {
    return updateByIdWithVersion(id, Map.of(column1, value1, column2, value2, column3, value3, column4, value4),
        version);
  }

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param column1
   * @param value1
   * @param column2
   * @param value2
   * @param column3
   * @param value3
   * @param column4
   * @param value4
   * @param column5
   * @param value5
   * @param version
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default boolean updateByIdWithVersion(K id, String column1, Object value1, String column2, Object value2,
      String column3, Object value3, String column4, Object value4, String column5, Object value5, Number version) {
    return updateByIdWithVersion(id, Map.of(column1, value1, column2, value2, column3, value3, column4, value4, column5,
        value5), version);
  }

  /**
   * 更新指定列，并判断版本，如果版本不对，则不更新，如果版本正确，则版本自动+1
   * @param id
   * @param columnValue
   * @param version
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  boolean updateByIdWithVersion(K id, Map<String, Object> columnValue, Number version);

  /**
   * 执行更新sql
   * @param updateSqlBuilder
   * @return 影响行数
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default int update(UpdateSqlBuilder updateSqlBuilder) {
    return update(updateSqlBuilder.getSql(), updateSqlBuilder.getParams());
  }

  /**
   * 执行更新sql，返回影响行数
   * @param sql sql语句
   * @param args sql参数
   * @return 影响行数
   * @throws DataAccessException
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  int update(String sql, @Nullable Object... args) throws DataAccessException;

  /**
   * 执行删除sql
   * @param deleteSqlBuilder
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  default int delete(DeleteSqlBuilder deleteSqlBuilder) {
    return update(deleteSqlBuilder.getSql(), deleteSqlBuilder.getParams());
  }
}
