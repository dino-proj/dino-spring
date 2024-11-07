// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.converts;

import cn.dinodev.spring.commons.Scope;

import jakarta.persistence.AttributeConverter;

/**
 *
 * @author Cody Lu
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
