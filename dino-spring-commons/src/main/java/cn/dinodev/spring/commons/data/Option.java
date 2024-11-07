// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
public class Option<V extends Serializable> implements ValueLabel<V> {

  /**
   * 选项类，用于表示一个选项的值和标签。
   *
   * @param <V> 选项值的类型
   */
  @Schema(description = "选项值", required = true)
  private V value;

  @Schema(description = "选项标签", required = true)
  private String label;

  @Schema(description = "选项的图标")
  @Nullable
  private String icon;

  @Schema(description = "选项的样式")
  @Nullable
  private String style;

  @Schema(description = "选项的描述")
  @Nullable
  private String desc;

  /**
   * 从 ValueLabel 对象创建一个 Option 对象。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl) {
    Option<V> option = new Option<>();
    option.setValue(vl.getValue());
    option.setLabel(vl.getLabel());
    return option;
  }

  /**
   * 从 ValueLabel 对象创建一个 Option 对象，并映射图标。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @param iconSupplier 图标提供者
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl, Supplier<String> iconSupplier) {
    Option<V> option = fromValueLabel(vl);
    option.setIcon(iconSupplier.get());
    return option;
  }

  /**
   * 从 ValueLabel 对象创建一个 Option 对象，并映射图标和样式。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @param iconSupplier 图标提供者
   * @param styleSupplier 样式提供者
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl, Supplier<String> iconSupplier,
      Supplier<String> styleSupplier) {
    Option<V> option = fromValueLabel(vl, iconSupplier);
    option.setStyle(styleSupplier.get());
    return option;
  }

  /**
   * 从 ValueLabel 对象创建一个 Option 对象，并映射图标、样式和描述。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @param iconSupplier 图标提供者
   * @param styleSupplier 样式提供者
   * @param descSupplier 描述提供者
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl, Supplier<String> iconSupplier,
      Supplier<String> styleSupplier, Supplier<String> descSupplier) {
    Option<V> option = fromValueLabel(vl, iconSupplier, styleSupplier);
    option.setDesc(descSupplier.get());
    return option;
  }

  /**
   * 从 ValueLabel 对象创建一个 Option 对象，并映射图标。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @param iconMapper 图标映射函数，传入选项value，返回图标
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl, Function<V, String> iconMapper) {
    Option<V> option = fromValueLabel(vl);
    option.setIcon(iconMapper.apply(vl.getValue()));
    return option;
  }

  /**
   * 从 ValueLabel 对象创建一个 Option 对象，并映射图标和样式。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @param iconMapper 图标映射函数，传入选项value，返回图标
   * @param styleMapper 样式映射函数，传入选项value，返回样式
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl, Function<V, String> iconMapper,
      Function<V, String> styleMapper) {
    Option<V> option = fromValueLabel(vl, iconMapper);
    option.setStyle(styleMapper.apply(vl.getValue()));
    return option;
  }

  /**
   * 从 ValueLabel 对象创建一个 Option 对象，并映射图标、样式和描述。
   *
   * @param <V> 选项值的类型
   * @param vl ValueLabel 对象
   * @param iconMapper 图标映射函数，传入选项value，返回图标
   * @param styleMapper 样式映射函数，传入选项value，返回样式
   * @param descMapper 描述映射函数，传入选项value，返回描述
   * @return Option 对象
   */
  public static <V extends Serializable> Option<V> fromValueLabel(ValueLabel<V> vl, Function<V, String> iconMapper,
      Function<V, String> styleMapper, Function<V, String> descMapper) {
    Option<V> option = fromValueLabel(vl, iconMapper, styleMapper);
    option.setDesc(descMapper.apply(vl.getValue()));
    return option;
  }

}
