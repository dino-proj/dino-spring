// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.binding;

import java.util.function.Function;

/**
 * 用户析出绑定的对象Id
 *
 * @author Cody LU
 * @date 2022-04-13 04:11:03
 */

public interface IdResolver {

  /**
   * 获取ID
   *
   * @param obj
   * @return
   */
  Function<?, ?> getIdGetter(Object obj);
}
