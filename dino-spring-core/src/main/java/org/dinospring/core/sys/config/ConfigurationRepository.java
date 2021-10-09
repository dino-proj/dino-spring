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

package org.dinospring.core.sys.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.dinospring.commons.Scope;
import org.dinospring.data.dao.CurdRepositoryBase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author tuuboo
 */

@NoRepositoryBean
public interface ConfigurationRepository<T extends Configuration> extends CurdRepositoryBase<T, Long> {

  /**
   * 根据Key查询一组属性
   * @param tenantId
   * @param key
   * @param scopes
   * @return
   */
  @Query("FROM #{#entityName} c WHERE c.tenantId = :tenantId and c.key = :key and c.scope in :scopes")
  List<T> queryPropertiesByKeyInScope(String tenantId, String key, @Nonnull Scope... scopes);

  /**
   * 查询一个属性
   *
   * @param tenantId
   * @param key
   * @param scopes
   * @return
   */
  default Optional<T> queryOnePropertyByKeyInScope(String tenantId, String key, @Nonnull final Scope... scopes) {
    var props = queryPropertiesByKeyInScope(tenantId, key, scopes);
    return props.stream().reduce((l, r) -> this.prorityProperty(l, r));
  }

  /**
   * 获取以group开头的配置
   * @param tenantId
   * @param group
   * @param scopes
   * @return
   */
  @Query("FROM #{#entityName} c WHERE tenantId = :tenantId and key like :group% and scope in :scopes")
  List<T> queryPropertiesByGroupInScope(String tenantId, String group, @Nonnull Scope... scopes);

  /**
   * 查询属性，并放入Map
   *
   * @param tenantId
   * @param group
   * @param scopes
   * @return
   */
  default Map<String, T> queryMapPropertiesByGroupInScope(String tenantId, String group, Scope... scopes) {
    var start = group.length();
    var props = queryPropertiesByGroupInScope(tenantId, group, scopes);
    return props.stream().collect(Collectors.groupingBy(p -> p.getKey().substring(start)))//
        .entrySet().stream().collect(Collectors.toMap(//
            Map.Entry::getKey, //
            e -> e.getValue().stream().reduce((l, r) -> this.prorityProperty(l, r)).get()));
  }

  /**
   * 属性优先级比较
   * @param l
   * @param r
   * @return
   */
  default T prorityProperty(T l, T r) {
    return l.getScope().getOrder() > r.getScope().getOrder() ? l : r;
  }

  /**
   * 删除属性
   * @param tenantId
   * @param key
   * @param scope
   * @param scopeValue
   */
  default void removeProperty(String tenantId, String key, @Nonnull Scope scope, String scopeValue) {
    queryOnePropertyByKeyInScope(tenantId, key, scope).ifPresent(c -> this.deleteById(c.getId()));
  }
}
