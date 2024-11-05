// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.data;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Cody LU
 */

public interface ValueLabel<V extends Serializable> {

  /**
   * 值
   * @return
   */
  @Schema(description = "值")
  V getValue();

  /**
   * Label标签
   * @return
   */
  @Schema(description = "Label标签")
  String getLabel();

}