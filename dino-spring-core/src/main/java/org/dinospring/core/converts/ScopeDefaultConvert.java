// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.converts;

import org.dinospring.commons.Scope;

import jakarta.persistence.AttributeConverter;

/**
 *
 * @author Cody LU
 */

public class ScopeDefaultConvert implements AttributeConverter<Scope, String> {

  @Override
  public String convertToDatabaseColumn(Scope attribute) {
    return attribute.getName();
  }

  @Override
  public Scope convertToEntityAttribute(String dbData) {
    return Scope.DEFAULT.of(dbData);
  }

}
