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
@JsonTypeName(CompBlock.T_NAME)
public class CompBlock<T extends Component> implements Component {
  public static final String T_NAME = "block";

  @Schema(description = "区块标题")
  private String title;

  @Schema(description = "是否显示标题")
  private boolean showTitle;

  @Schema(description = "是否显示更多")
  private Boolean showMore;

  @Schema(description = "更多的跳转连接")
  private CompLink moreLink;

  @Schema(description = "区块里的内容")
  private T content;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
