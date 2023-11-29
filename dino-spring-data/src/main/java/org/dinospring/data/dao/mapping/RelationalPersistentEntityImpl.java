// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.dao.mapping;

import java.util.Optional;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.util.Lazy;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.StringUtils;

import jakarta.persistence.Table;

/**
 * Default implementation of {@link RelationalPersistentEntity}.
 * @author Cody Lu
 * @date 2023-11-30 02:29:58
 */

public class RelationalPersistentEntityImpl<T> extends BasicPersistentEntity<T, RelationalPersistentProperty>
    implements RelationalPersistentEntity<T> {

  private final NamingStrategy namingStrategy;
  private final Lazy<Optional<SqlIdentifier>> tableNameLazy;
  private boolean forceQuote = true;

  /**
  * Creates a new {@link RelationalPersistentEntityImpl} for the given {@link TypeInformation}.
  *
  * @param information must not be {@literal null}.
  */
  public RelationalPersistentEntityImpl(TypeInformation<T> information, NamingStrategy namingStrategy) {

    super(information);

    this.namingStrategy = namingStrategy;
    this.tableNameLazy = Lazy.of(() -> Optional.ofNullable( //
        this.findAnnotation(Table.class)) //
        .map(Table::name) //
        .filter(StringUtils::hasText) //
        .map(this::createSqlIdentifier) //
    );
  }

  private SqlIdentifier createSqlIdentifier(String name) {
    return this.isForceQuote() ? SqlIdentifier.quoted(name) : SqlIdentifier.unquoted(name);
  }

  private SqlIdentifier createDerivedSqlIdentifier(String name) {
    return new DerivedSqlIdentifier(name, this.isForceQuote());
  }

  public boolean isForceQuote() {
    return this.forceQuote;
  }

  public void setForceQuote(boolean forceQuote) {
    this.forceQuote = forceQuote;
  }

  @Override
  public SqlIdentifier getTableName() {
    return this.tableNameLazy.get().orElseGet(() -> {

      String schema = this.namingStrategy.getSchema();
      SqlIdentifier tableName = this.createDerivedSqlIdentifier(this.namingStrategy.getTableName(this.getType()));

      return StringUtils.hasText(schema) ? SqlIdentifier.from(this.createDerivedSqlIdentifier(schema), tableName)
          : tableName;
    });
  }

  @Override
  public SqlIdentifier getIdColumn() {
    return this.getRequiredIdProperty().getColumnName();
  }

  @Override
  public String toString() {
    return String.format("RelationalPersistentEntityImpl<%s>", this.getType());
  }
}
