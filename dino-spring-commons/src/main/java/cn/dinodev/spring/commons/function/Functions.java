// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.function;

import java.util.function.Function;

import cn.dinodev.spring.commons.utils.TypeUtils;

/**
 * 函数工具接口，提供函数式编程的便捷方法
 *
 * @author Cody Lu
 * @since 2022-06-09
 */

public interface Functions {
  static Function<Object, Object> IDENTITY_FUNCTION = t -> t;

  /**
   * 创建一个Function，该Function将传入的参数原样返回
   * @param <T>
   * @return
   */
  static <T> Function<T, T> identity() {
    return TypeUtils.cast(IDENTITY_FUNCTION);
  }

}
