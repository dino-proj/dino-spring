// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 值标签接口，定义了获取值和标签的通用方法
 * @param <V> 值的类型，必须实现Serializable接口
 * @author Cody Lu
 */

public interface ValueLabel<V extends Serializable> {

  /**
   * 值
   * @return 返回值对象
   */
  @Schema(description = "值")
  V getValue();

  /**
   * Label标签
   * @return 返回标签字符串
   */
  @Schema(description = "Label标签")
  String getLabel();

}