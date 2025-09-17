// Copyright 2023 dinodev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc;

import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import cn.dinodev.spring.data.jdbc.mapping.DinoJdbcPersistentEntity;
import cn.dinodev.spring.data.jdbc.mapping.DinoJdbcPersistentProperty;

/**
 *
 * @author Cody Lu
 * @date 2023-05-18 00:21:40
 */

public class DinoJdbcMappingContext extends JdbcMappingContext {
  private static final String MISSING_PARAMETER_NAME = "A constructor parameter name must not be null to be used with Spring Data JDBC! Offending parameter: %s";

  public DinoJdbcMappingContext(NamingStrategy namingStrategy) {
    super(namingStrategy);
  }

  @Override
  @NonNull
  protected <T> RelationalPersistentEntity<T> createPersistentEntity(@NonNull TypeInformation<T> typeInformation) {
    DinoJdbcPersistentEntity<T> entity = new DinoJdbcPersistentEntity<>(typeInformation,
        this.getNamingStrategy());
    entity.setForceQuote(this.isForceQuote());
    PreferredConstructor<T, RelationalPersistentProperty> constructor = entity.getPersistenceConstructor();

    if (constructor == null) {
      return entity;
    }

    for (var parameter : constructor.getParameters()) {
      Assert.state(StringUtils.hasText(parameter.getName()), () -> String.format(MISSING_PARAMETER_NAME, parameter));
    }

    return entity;
  }

  @Override
  @NonNull
  protected RelationalPersistentProperty createPersistentProperty(@NonNull Property property,
      @NonNull RelationalPersistentEntity<?> owner, @NonNull SimpleTypeHolder simpleTypeHolder) {
    var persistentProperty = new DinoJdbcPersistentProperty(property, owner, simpleTypeHolder,
        this.getNamingStrategy());
    persistentProperty.setForceQuote(this.isForceQuote());
    return persistentProperty;
  }

  @Override
  protected boolean shouldCreatePersistentEntityFor(@NonNull TypeInformation<?> type) {
    return super.shouldCreatePersistentEntityFor(type)//
        && (type.getType().isAnnotationPresent(jakarta.persistence.Table.class) ||
            type.getType().isAnnotationPresent(org.springframework.data.relational.core.mapping.Table.class));
  }
}
