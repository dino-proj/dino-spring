package org.dinospring.data.autoconfig;

import javax.persistence.Table;

import org.dinospring.data.dao.mapping.RelationalPersistentEntityImpl;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.PreferredConstructor.Parameter;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class DinoJdbcMappingContext extends JdbcMappingContext {
  private static final String MISSING_PARAMETER_NAME = "A constructor parameter name must not be null to be used with Spring Data JDBC! Offending parameter: %s";

  public DinoJdbcMappingContext(NamingStrategy namingStrategy) {
    super(namingStrategy);
  }

  @Override
  protected <T> RelationalPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
    RelationalPersistentEntityImpl<T> entity = new RelationalPersistentEntityImpl<>(typeInformation,
        this.getNamingStrategy());
    entity.setForceQuote(isForceQuote());
    PreferredConstructor<T, RelationalPersistentProperty> constructor = entity.getPersistenceConstructor();

    if (constructor == null) {
      return entity;
    }

    for (Parameter<Object, RelationalPersistentProperty> parameter : constructor.getParameters()) {
      Assert.state(StringUtils.hasText(parameter.getName()), () -> String.format(MISSING_PARAMETER_NAME, parameter));
    }

    return entity;
  }

  @Override
  protected RelationalPersistentProperty createPersistentProperty(Property property,
      RelationalPersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
    // TODO Auto-generated method stub
    return super.createPersistentProperty(property, owner, simpleTypeHolder);
  }

  @Override
  protected boolean shouldCreatePersistentEntityFor(TypeInformation<?> type) {
    return super.shouldCreatePersistentEntityFor(type)//
        & type.getType().getAnnotation(Table.class) != null;
  }
}
