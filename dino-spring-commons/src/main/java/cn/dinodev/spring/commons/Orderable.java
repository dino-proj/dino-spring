// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;

/**
 * 可排序接口，为对象提供排序码功能
 * 
 * @author Cody Lu
 */

public interface Orderable {

  /**
   * 获取排序码
   * 
   * @return 排序码，数值越小排序越靠前
   */
  @Schema(description = "排序号")
  @Column(name = "order_num", nullable = true)
  Integer getOrderNum();
}
