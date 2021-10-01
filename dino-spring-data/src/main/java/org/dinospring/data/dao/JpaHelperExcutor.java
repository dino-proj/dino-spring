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

import java.util.Map;

import javax.persistence.EntityManager;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.data.jpa.repository.Modifying;

public interface JpaHelperExcutor<T, K> {

  EntityManager getEntityManager();

  Class<T> getEntityClass();

  Class<K> getKeyClass();

  default String tableName() {
    return tableName(getEntityClass());
  }

  <C> String tableName(Class<C> cls);

  String toJson(Object obj) throws JsonProcessingException;

  <C> C fromJson(String json, Class<C> cls) throws JsonProcessingException;

  @Modifying
  default boolean updateColumnById(K id, String column, Object value) {
    return updateColumnById(id, Map.of(column, value));
  }

  @Modifying
  default boolean updateColumnById(K id, String column1, Object value1, String column2, Object value2) {
    return updateColumnById(id, Map.of(column1, value1, column2, value2));
  }

  @Modifying
  boolean updateColumnById(K id, Map<String, Object> columnValue);
}
