// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import cn.dinodev.spring.commons.Scope;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author Cody Lu
 */

@NoRepositoryBean
public interface ConfigurationRepository<T extends Configuration> extends CrudRepositoryBase<T, Long> {

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
    return props.stream().reduce(this::prorityProperty);
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
            e -> e.getValue().stream().reduce(this::prorityProperty).get()));
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
