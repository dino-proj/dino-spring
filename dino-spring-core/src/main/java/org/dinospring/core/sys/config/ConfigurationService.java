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
