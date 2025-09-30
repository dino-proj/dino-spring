// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller.support;

import java.io.Serializable;

/**
 * @author JL
 * @Date: 2021/11/2
 */
public interface FieldEnum extends Serializable {

  /**
   * 数据库字段名
   * @return
   */
  String getField();

}
