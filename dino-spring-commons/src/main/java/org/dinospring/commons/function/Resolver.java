// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.function;

/**
 * 资源解析器接口。
 * @author Cody Lu
 * @date 2024-02-03 01:53:52
 */

public interface Resolver<T> {

  /**
   * 是否支持解析指定的资源。
   * @param what 资源
   * @return true 支持，false 不支持
   */
  boolean isSupported(T what);

  /**
   * 解析资源。
   * @param what 资源
   * @return 解析后的资源
   */
  T resolve(T what);
}
