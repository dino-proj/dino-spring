// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.commons.data.ValueLabel;
import org.dinospring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody LU
 */

@Data
@JsonTypeName(CompOption.T_NAME)
public class CompOption implements Component, ValueLabel<String> {
  public static final String T_NAME = "option";

  @Schema(description = "选项值", required = true)
  private String value;

  @Schema(description = "选项标签", required = true)
  private String label;

  @Schema(description = "选项的图标")
  private String icon;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }

}
