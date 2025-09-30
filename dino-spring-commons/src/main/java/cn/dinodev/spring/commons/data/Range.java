// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

/**
 * 范围接口，定义了获取开始和结束值的通用方法
 * @param <T> 范围值的类型，必须实现Serializable接口
 * @author Cody Lu
 * @since 2022-03-07
 */

public interface Range<T extends Serializable> extends Serializable {
  /**
   * 开始
   * @return 返回开始值
   */
  T getBegin();

  /**
   * 结束
   * @return 返回结束值
   */
  T getEnd();
}
