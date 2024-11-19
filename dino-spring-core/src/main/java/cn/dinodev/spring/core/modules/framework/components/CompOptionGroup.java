// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework.components;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import cn.dinodev.spring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
@JsonTypeName(CompOptionGroup.T_NAME)
public class CompOptionGroup implements Component {
  public static final String T_NAME = "option-group";

  @Schema(description = "组的名字", required = true)
  private String name;

  @Schema(description = "组的图标")
  private String icon;

  @Schema(description = "选项")
  private List<CompOption> options;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }

}