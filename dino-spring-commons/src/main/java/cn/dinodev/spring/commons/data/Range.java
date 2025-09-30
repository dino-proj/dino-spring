// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

/**
 *
 * @author Cody Lu
 * @since 2022-03-07
 */

public interface Range<T extends Serializable> extends Serializable {
  /**
   * 开始
   * @return
   */
  T getBegin();

  /**
   * 结束
   * @return
   */
  T getEnd();
}
