// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.binding;

import java.util.Collection;

/**
 * binder 接口
 * @author Cody LU
 * @date 2022-04-13 04:11:45
 */

public interface Binder<K> {

  /**
   * 绑定单个对象
   *
   * @param target
   */
  <T> void bind(T target);

  /**
   * 批量绑定一组对象
   *
   * @param targets
   */
  <T> void bindBatch(Collection<T> targets);
}
