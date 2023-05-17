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

package org.dinospring.data.dao;

import java.util.Map;

import org.dinospring.data.sql.builder.DeleteSqlBuilder;
import org.dinospring.data.sql.builder.UpdateSqlBuilder;
import org.springframework.data.jdbc.repository.query.Modifying;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *
 * @author tuuboo
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
   * 将对象转为Json
   * @param obj
   * @return
   * @throws JsonProcessingException
   */
  String toJson(Object obj) throws JsonProcessingException;

  /**
   * 从Json中反序列化对象
   * @param <C>
   * @param json
   * @param cls
   * @return
   * @throws JsonProcessingException
   */
  <C> C fromJson(String json, Class<C> cls) throws JsonProcessingException;

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
