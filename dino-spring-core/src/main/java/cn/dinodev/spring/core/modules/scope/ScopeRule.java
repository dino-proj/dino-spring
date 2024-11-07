// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.scope;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 16:50:48
 */

public interface ScopeRule extends Serializable {

  /**
   * 转为Json串
   * @param objectMapper
   * @return
   */
  String toJson(ObjectMapper objectMapper);

  /**
   * 计算规则的Hash值
   * @return
   */
  String hash();
}
