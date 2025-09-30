// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.binding;

import java.util.Collection;

/**
 * binder 接口
 * @param <K> 绑定键的类型
 * @author Cody Lu
 * @since 2022-04-13
 */

public interface Binder<K> {

  /**
   * 绑定单个对象
   *
   * @param <T> 目标对象类型
   * @param target 要绑定的目标对象
   */
  <T> void bind(T target);

  /**
   * 批量绑定一组对象
   *
   * @param <T> 目标对象类型
   * @param targets 要绑定的目标对象集合
   */
  <T> void bindBatch(Collection<T> targets);
}
