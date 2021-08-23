package org.dinospring.core.sys.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.Scope;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.core.service.impl.ServiceBase;

public abstract class ConfigurationService<T extends Configuration> extends ServiceBase<T, Long> {

  public abstract ConfigurationRepository<T> getConfigRepository();

  public Property getProperty(String key, @Nonnull final Scope scope, String scopeValue) {
    var config = getConfigRepository().queryOnePropertyByKeyInScope(ContextHelper.currentTenantId(), key,
        scope.lowerScopes(true));
    return this.projection(Property.class, config);
  }

  public Map<String, Property> getGroupProperties(String group, @Nonnull final Scope scope, String scopeValue) {
    final Map<String, Property> props = new HashMap<>();
    group = StringUtils.appendIfMissing(group, ".", ".");
    var config = getConfigRepository().queryMapPropertiesByGroupInScope(ContextHelper.currentTenantId(), group,
        scope.lowerScopes(true));
    config.forEach((k, v) -> props.put(k, this.projection(Property.class, v)));
    return projection(Property.class, config);
  }

  public Property setProperty(String key, @Nonnull final Scope scope, String scopeValue, Object value) {
    var config = getConfigRepository().queryOnePropertyByKeyInScope(ContextHelper.currentTenantId(), key, scope)
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
