// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;

/**
 *
 * @author Cody LU
 */

public interface Orderable {

  /**
   * 排序码
   * @return
   */
  @Schema(description = "排序号")
  @Column(name = "order_num", nullable = true)
  Integer getOrderNum();
}
