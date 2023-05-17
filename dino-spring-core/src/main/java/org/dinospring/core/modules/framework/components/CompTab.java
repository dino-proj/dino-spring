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
 * @author tuuboo
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
