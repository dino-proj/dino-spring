// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.converts;

import java.util.Set;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.sys.UserType;
import cn.dinodev.spring.core.sys.user.UserServiceProvider;
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
