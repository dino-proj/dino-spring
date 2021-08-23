package org.dinospring.core.converts;

import javax.persistence.AttributeConverter;

import org.dinospring.commons.Scope;

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
