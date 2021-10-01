// Copyright 2021 dinospring.cn
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
