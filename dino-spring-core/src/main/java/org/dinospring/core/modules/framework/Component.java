// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 *
 * @author Cody LU
 */

public interface Component extends Serializable {
  /**
   * 组件名
   * @return
   */
  @Schema(description = "组件名")
  @JsonProperty(value = "@t", access = Access.READ_ONLY)
  String getComponentName();

  /**
   * 入参处理
   */
  default void processReq() {

  }

  /**
   * 出参处理
   */
  default void processVo() {

  }
}
