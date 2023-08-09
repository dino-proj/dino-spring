// Copyright 2021 dinodev.cn
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

import java.util.Set;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.sys.UserType;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Cody LU
 */
@Component
@Converter(autoApply = true)
public class UserTypeStringConverter implements AttributeConverter<UserType, String>, GenericConverter {

  @Override
  public String convertToDatabaseColumn(UserType attribute) {
    return attribute.getType();
  }

  @Override
  public UserType convertToEntityAttribute(String dbData) {
    return ContextHelper.findBean(UserServiceProvider.class).resolveUserType(dbData);
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Set.of(new ConvertiblePair(String.class, UserType.class), //
        new ConvertiblePair(UserType.class, String.class));
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (source == null) {
      return null;
    }
    if (source instanceof String) {
      return convertToEntityAttribute(CastUtils.cast(source));
    } else {
      return convertToDatabaseColumn(CastUtils.cast(source));
    }
  }

}
