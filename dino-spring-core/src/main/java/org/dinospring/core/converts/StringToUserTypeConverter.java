// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.converts;

import java.util.Set;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.sys.UserType;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cody Lu
 */
@Component
@ReadingConverter
public class StringToUserTypeConverter implements GenericConverter {

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Set.of(new ConvertiblePair(String.class, UserType.class));
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (source == null) {
      return null;
    }
    return ContextHelper.findBean(UserServiceProvider.class).resolveUserType(CastUtils.cast(source));

  }

}
