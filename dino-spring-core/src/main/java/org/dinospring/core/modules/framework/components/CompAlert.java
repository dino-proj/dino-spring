// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody LU
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
