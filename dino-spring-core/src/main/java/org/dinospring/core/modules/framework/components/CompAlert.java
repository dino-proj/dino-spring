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
@JsonTypeName(CompAlert.T_NAME)
public class CompAlert implements Component, Action {
  public static final String T_NAME = "alert";

  @Schema(description = "提示窗标题")
  private String title;

  @Schema(description = "提示窗类型")
  private AlertType type;

  @Schema(description = "提示内容")
  private String msg;

  @Schema(description = "按钮文字")
  private String buttonLabel;

  public enum AlertType {
    //警告
    WARN,
    //错误
    ERROR,
    //信息
    INFO;
  }

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
