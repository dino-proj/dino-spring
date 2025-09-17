// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import cn.dinodev.spring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
@JsonTypeName(CompButton.T_NAME)
public class CompButton implements Component {
  public static final String T_NAME = "button";

  @Schema(description = "按钮标签")
  private String label;

  @Schema(description = "按钮的图标")
  private String icon;

  @Schema(description = "执行动作", oneOf = { CompLink.class, CompAlert.class })
  private Action action;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
