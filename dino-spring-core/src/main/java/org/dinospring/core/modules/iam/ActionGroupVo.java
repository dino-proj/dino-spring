// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.iam;

import java.util.List;

import org.dinospring.core.vo.VoImplBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Cody LU
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActionGroupVo extends VoImplBase<Long> {

  @Schema(description = "权限组的名字")
  private String name;

  @Schema(description = "权限组的描述")
  private String remark;

  @Schema(description = "包含的权限")
  private List<Action> actions;

}
