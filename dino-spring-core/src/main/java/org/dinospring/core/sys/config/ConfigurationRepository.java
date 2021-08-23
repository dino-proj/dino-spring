package org.dinospring.core.sys.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.dinospring.commons.Scope;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ConfigurationRepository<T extends Configuration> extends CURDRepositoryBase<T, Long> {

  @Query("FROM #{#entityName} c WHERE c.tenantId = :tenantId and c.key = :key and c.scope in :scopes")
  List<T> queryPropertiesByKeyInScope(String tenantId, String key, @Nonnull Scope... scopes);

  default Optional<T> queryOnePropertyByKeyInScope(String tenantId, String key, @Nonnull final Scope... scopes) {
    var props = queryPropertiesByKeyInScope(tenantId, key, scopes);
    return props.stream().reduce((l, r) -> this.prorityProperty(l, r));
  }

  @Query("FROM #{#entityName} c WHERE tenantId = :tenantId and key like :group% and scope in :scopes")
  List<T> queryPropertiesByGroupInScope(String tenantId, String group, @Nonnull Scope... scopes);

  default Map<String, T> queryMapPropertiesByGroupInScope(String tenantId, String group, Scope... scopes) {
    var start = group.length();
    var props = queryPropertiesByGroupInScope(tenantId, group, scopes);
    return props.stream().collect(Collectors.groupingBy(p -> p.getKey().substring(start)))//
        .entrySet().stream().collect(Collectors.toMap(//
            Map.Entry::getKey, //
            e -> e.getValue().stream().reduce((l, r) -> this.prorityProperty(l, r)).get()));
  }

  default T prorityProperty(T l, T r) {
    return l.getScope().getOrder() > r.getScope().getOrder() ? l : r;
  }

  default void removeProperty(String tenantId, String key, @Nonnull Scope scope, String scopeValue) {
    queryOnePropertyByKeyInScope(tenantId, key, scope).ifPresent(c -> this.deleteById(c.getId()));
  }
}
