// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */

@Data
@JsonTypeName(CompTab.T_NAME)
public class CompTab<T extends Component> implements Component {
  public static final String T_NAME = "tab";

  @Schema(description = "ID")
  private String id;

  @Schema(description = "Tab项标签")
  private String label;

  @Schema(description = "Tab项的图标")
  private String icon;

  @Schema(description = "Tab页里的内容")
  private T content;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }

  @Override
  public void processVo() {
    content.processVo();
  }

  @Override
  public void processReq() {
    content.processReq();
  }
}
