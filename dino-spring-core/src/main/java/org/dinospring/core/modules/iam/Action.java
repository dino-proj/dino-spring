// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.iam;

import java.io.Serializable;

import lombok.extern.jackson.Jacksonized;
import org.dinospring.commons.data.ValueLabel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Cody Lu
 * @date 2022-04-12 14:21:19
 */

@Data
@Builder
@Jacksonized
public class Action implements ValueLabel<String>, Serializable {

  @Schema(description = "权限：如user:create，多个操作用逗号分隔，如user:create,update")
  private String value;

  @Schema(description = "权限名字：如 “创建")
  private String label;

}
