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

public class RelationalPersistentEntityImpl<T> extends BasicPersistentEntity<T, RelationalPersistentProperty>
    implements RelationalPersistentEntity<T> {

  private final NamingStrategy namingStrategy;
  private final Lazy<Optional<SqlIdentifier>> tableName;
  private boolean forceQuote = true;

  /**
  * Creates a new {@link RelationalPersistentEntityImpl} for the given {@link TypeInformation}.
  *
  * @param information must not be {@literal null}.
  */
  public RelationalPersistentEntityImpl(TypeInformation<T> information, NamingStrategy namingStrategy) {

    super(information);

    this.namingStrategy = namingStrategy;
    this.tableName = Lazy.of(() -> Optional.ofNullable( //
        findAnnotation(Table.class)) //
        .map(Table::name) //
        .filter(StringUtils::hasText) //
        .map(this::createSqlIdentifier) //
    );
  }

  private SqlIdentifier createSqlIdentifier(String name) {
    return isForceQuote() ? SqlIdentifier.quoted(name) : SqlIdentifier.unquoted(name);
  }

  private SqlIdentifier createDerivedSqlIdentifier(String name) {
    return new DerivedSqlIdentifier(name, isForceQuote());
  }

  public boolean isForceQuote() {
    return forceQuote;
  }

  public void setForceQuote(boolean forceQuote) {
    this.forceQuote = forceQuote;
  }

  /*
  * (non-Javadoc)
  * @see org.springframework.data.relational.mapping.model.RelationalPersistentEntity#getTableName()
  */
  @Override
  public SqlIdentifier getTableName() {
    return tableName.get().orElseGet(() -> {

      String schema = namingStrategy.getSchema();
      SqlIdentifier tableName = createDerivedSqlIdentifier(namingStrategy.getTableName(getType()));

      return StringUtils.hasText(schema) ? SqlIdentifier.from(createDerivedSqlIdentifier(schema), tableName)
          : tableName;
    });
  }

  /*
  * (non-Javadoc)
  * @see org.springframework.data.relational.core.mapping.model.RelationalPersistentEntity#getIdColumn()
  */
  @Override
  public SqlIdentifier getIdColumn() {
    return getRequiredIdProperty().getColumnName();
  }

  /*
  * (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  @Override
  public String toString() {
    return String.format("RelationalPersistentEntityImpl<%s>", getType());
  }
}
