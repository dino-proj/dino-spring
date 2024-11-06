// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.token;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenPrincaple implements Serializable {

  @Schema(description = "租户ID")
  private String tenantId;

  @Schema(description = "用户类型")
  private String userType;

  @Schema(description = "用户ID")
  private String userId;

  @Schema(description = "平台")
  private String plt;

  @Schema(description = "设备ID")
  private String guid;
}
