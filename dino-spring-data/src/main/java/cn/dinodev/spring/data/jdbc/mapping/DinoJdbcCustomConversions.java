// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc.mapping;

import java.util.List;
import java.util.Optional;

import org.springframework.data.convert.ValueConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.lang.NonNull;

/**
 *
 * @author Cody Lu
 * @date 2024-01-08 06:09:47
 */

public class DinoJdbcCustomConversions extends JdbcCustomConversions {
  public DinoJdbcCustomConversions(StoreConversions storeConversions, List<?> userConverters) {
    super(storeConversions, userConverters);
  }

  @Override
  public boolean hasValueConverter(@NonNull PersistentProperty<?> property) {
    return property.isAnnotationPresent(ValueConverter.class);
  }

  @Override
  @NonNull
  public Optional<Class<?>> getCustomWriteTarget(@NonNull Class<?> sourceType) {
    // TODO Auto-generated method stub
    // simple 类型返回null
    return super.getCustomWriteTarget(sourceType);
  }
}
