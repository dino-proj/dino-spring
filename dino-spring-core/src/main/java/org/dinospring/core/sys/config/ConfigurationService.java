// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.config;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.Scope;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.core.service.impl.ServiceBase;

/**
 *
 * @author Cody LU
 */

public abstract class ConfigurationService<T extends Configuration> extends ServiceBase<T, Long> {

  /**
   * configRepository
   *
   * @return
   */
  public abstract ConfigurationRepository<T> configRepository();

  /**
   * 获取指定配置
   * @param key
   * @param scope
   * @param scopeValue
   * @return
   */
  public Property getProperty(String key, @Nonnull final Scope scope, String scopeValue) {
    var config = configRepository().queryOnePropertyByKeyInScope(ContextHelper.currentTenantId(), key,
        scope.lowerScopes(true));
    return this.projection(Property.class, config);
  }

  /**
   * 获取以group开头的配置
   * @param group
   * @param scope
   * @param scopeValue
   * @return
   */
  public Map<String, Property> getGroupProperties(String group, @Nonnull final Scope scope, String scopeValue) {
    final Map<String, Property> props = new HashMap<>(8);
    group = StringUtils.appendIfMissing(group, ".", ".");
    var config = configRepository().queryMapPropertiesByGroupInScope(ContextHelper.currentTenantId(), group,
        scope.lowerScopes(true));
    config.forEach((k, v) -> props.put(k, this.projection(Property.class, v)));
    return projection(Property.class, config);
  }

  /**
   * 保存配置
   * @param key
   * @param scope
   * @param scopeValue
   * @param value
   * @return
   */
  public Property setProperty(String key, @Nonnull final Scope scope, String scopeValue, Object value) {
    var config = configRepository().queryOnePropertyByKeyInScope(ContextHelper.currentTenantId(), key, scope)
        .orElseGet(() -> {
          var entity = newEntity();
          entity.setKey(key).setScope(scope).setScopeValue(scopeValue).setTenantId(ContextHelper.currentTenantId());
          return entity;
        });

    config.setValue(value);
    this.save(config);
    return this.projection(Property.class, config);
  }

}
