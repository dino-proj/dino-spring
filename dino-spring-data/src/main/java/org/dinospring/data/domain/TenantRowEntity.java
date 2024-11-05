// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 基于Row的Tenant实体接口，
 * @author Cody LU
 */
public interface TenantRowEntity {

  /**
   * 获取租户ID
   * @return
   */
  @Schema(description = "租户ID")
  String getTenantId();

  /**
   * 设置租户ID
   * @param tenantId 租户ID
   */
  void setTenantId(String tenantId);
}
