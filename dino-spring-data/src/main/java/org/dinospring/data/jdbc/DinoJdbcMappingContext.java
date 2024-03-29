// Copyright 2023 dinodev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.jdbc;

import org.dinospring.data.jdbc.mapping.DinoJdbcPersistentEntity;
import org.dinospring.data.jdbc.mapping.DinoJdbcPersistentProperty;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
  protected <T> RelationalPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
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
  protected RelationalPersistentProperty createPersistentProperty(Property property,
      RelationalPersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
    var persistentProperty = new DinoJdbcPersistentProperty(property, owner, simpleTypeHolder,
        this.getNamingStrategy());
    persistentProperty.setForceQuote(this.isForceQuote());
    return persistentProperty;
  }

  @Override
  protected boolean shouldCreatePersistentEntityFor(TypeInformation<?> type) {
    return super.shouldCreatePersistentEntityFor(type)//
        && (type.getType().isAnnotationPresent(jakarta.persistence.Table.class) ||
            type.getType().isAnnotationPresent(org.springframework.data.relational.core.mapping.Table.class));
  }
}
