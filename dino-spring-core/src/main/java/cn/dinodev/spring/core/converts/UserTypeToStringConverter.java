// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.converts;

import java.util.Set;

import cn.dinodev.spring.commons.sys.UserType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cody Lu
 */
@Component
@WritingConverter
public class UserTypeToStringConverter implements GenericConverter {

  private String convertToDatabaseColumn(UserType attribute) {
    return attribute.getType();
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Set.of(new ConvertiblePair(UserType.class, String.class));
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (source == null) {
      return null;
    }
    return this.convertToDatabaseColumn(CastUtils.cast(source));

  }

}
