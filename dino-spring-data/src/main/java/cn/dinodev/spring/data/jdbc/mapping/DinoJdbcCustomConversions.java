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
  /**
   * 创建DinoJdbcCustomConversions实例
   * @param storeConversions 存储转换配置
   * @param userConverters 用户自定义转换器列表
   */
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
    // simple 类型返回null
    return super.getCustomWriteTarget(sourceType);
  }
}
