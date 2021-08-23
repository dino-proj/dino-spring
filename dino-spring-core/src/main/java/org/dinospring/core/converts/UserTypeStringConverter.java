package org.dinospring.core.converts;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.dinospring.commons.sys.UserType;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;

@Converter(autoApply = true)
public class UserTypeStringConverter implements AttributeConverter<UserType, String> {
  @Autowired
  private UserServiceProvider userServiceProvider;

  @Override
  public String convertToDatabaseColumn(UserType attribute) {
    return attribute.getType();
  }

  @Override
  public UserType convertToEntityAttribute(String dbData) {
    return userServiceProvider.resolveUserType(dbData);
  }

}
