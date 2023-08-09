// Copyright 2023 dinodev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.dao.mapping;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.UnaryOperator;

import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.util.Assert;

/**
 *
 * @author Cody Lu
 * @date 2023-05-17 22:08:33
 */

public class DerivedSqlIdentifier implements SqlIdentifier {

  private final String name;
  private final boolean quoted;

  DerivedSqlIdentifier(String name, boolean quoted) {

    Assert.hasText(name, "A database object must have at least on name part.");
    this.name = name;
    this.quoted = quoted;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.relational.domain.SqlIdentifier#iterator()
   */
  @Override
  public Iterator<SqlIdentifier> iterator() {
    return Collections.<SqlIdentifier>singleton(this).iterator();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.relational.domain.SqlIdentifier#transform(java.util.function.UnaryOperator)
   */
  @Override
  public SqlIdentifier transform(UnaryOperator<String> transformationFunction) {

    Assert.notNull(transformationFunction, "Transformation function must not be null");

    return new DerivedSqlIdentifier(transformationFunction.apply(name), quoted);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.relational.domain.SqlIdentifier#toSql(org.springframework.data.relational.domain.IdentifierProcessing)
   */
  @Override
  public String toSql(IdentifierProcessing processing) {

    String normalized = processing.standardizeLetterCase(name);

    return quoted ? processing.quote(normalized) : normalized;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.relational.domain.SqlIdentifier#getReference(org.springframework.data.relational.domain.IdentifierProcessing)
   */
  @Override
  public String getReference(IdentifierProcessing processing) {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (o instanceof SqlIdentifier) {
      return toString().equals(o.toString());
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return quoted ? toSql(IdentifierProcessing.ANSI) : this.name;
  }
}
