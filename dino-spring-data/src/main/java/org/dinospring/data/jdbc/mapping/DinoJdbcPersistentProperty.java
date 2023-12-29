// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.jdbc.mapping;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jdbc.core.mapping.BasicJdbcPersistentProperty;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.util.Lazy;

/**
 * DinoJdbc Persistent Property.
 * support jakarta persistence annotation, such as @Table, @Column, @Id, @GeneratedValue, @Embedded, @Transient
 *
 * @author Cody Lu
 * @date 2023-12-28 09:12:22
 */

public class DinoJdbcPersistentProperty extends BasicJdbcPersistentProperty {

  private final Lazy<Boolean> isId;
  private final Lazy<Boolean> isVersion;
  private final Lazy<Boolean> isEmbedded;
  private final Lazy<SqlIdentifier> columnName;
  private final Lazy<Boolean> isInsertable;
  private final Lazy<Boolean> isUpdatable;
  private final Lazy<Boolean> isTransient;

  public DinoJdbcPersistentProperty(Property property, PersistentEntity<?, RelationalPersistentProperty> owner,
      SimpleTypeHolder simpleTypeHolder, NamingStrategy namingStrategy) {
    super(property, owner, simpleTypeHolder, namingStrategy);

    this.isId = Lazy.of(() -> this.isAnnotationPresent(jakarta.persistence.Id.class) || super.isIdProperty());

    this.isVersion = Lazy
        .of(() -> this.isAnnotationPresent(jakarta.persistence.Version.class) || super.isVersionProperty());

    this.isEmbedded = Lazy.of(() -> this.isAnnotationPresent(jakarta.persistence.Embedded.class) || super.isEmbedded());

    this.columnName = Lazy.of(() -> Optional.ofNullable(this.findAnnotation(jakarta.persistence.Column.class)) //
        .map(jakarta.persistence.Column::name) //
        .filter(StringUtils::isNotBlank) //
        .map(this::createSqlIdentifier) //
        .orElseGet(super::getColumnName));

    this.isInsertable = Lazy.of(() -> Optional.ofNullable(this.findAnnotation(jakarta.persistence.Column.class)) //
        .map(jakarta.persistence.Column::insertable) //
        .orElse(true));

    this.isUpdatable = Lazy.of(() -> Optional.ofNullable(this.findAnnotation(jakarta.persistence.Column.class)) //
        .map(jakarta.persistence.Column::updatable) //
        .orElseGet(() -> !super.isInsertOnly()));

    this.isTransient = Lazy
        .of(() -> this.isAnnotationPresent(jakarta.persistence.Transient.class) || super.isTransient());

  }

  @Override
  public boolean isIdProperty() {
    return this.isId.get();
  }

  @Override
  public boolean isVersionProperty() {
    return this.isVersion.get();
  }

  @Override
  public SqlIdentifier getColumnName() {
    return this.columnName.get();
  }

  @Override
  public boolean isEmbedded() {
    return this.isEmbedded.get();
  }

  @Override
  public boolean isInsertOnly() {
    return this.isInsertable.get() && !this.isUpdatable.get();
  }

  @Override
  public boolean isTransient() {
    return this.isTransient.get();
  }

  private SqlIdentifier createSqlIdentifier(String name) {
    return this.isForceQuote() ? SqlIdentifier.quoted(name) : SqlIdentifier.unquoted(name);
  }
}
