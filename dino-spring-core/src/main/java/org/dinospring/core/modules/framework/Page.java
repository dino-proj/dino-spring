// Copyright 2021 dinospring.cn
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

package org.dinospring.core.modules.framework;

import org.dinospring.core.modules.framework.template.Template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Page<T extends PageConfig> {
  @Schema(description = "页面ID")
  private Long id;

  @Schema(description = "页面标题")
  private String title;

  @Schema(description = "页面对应的模板")
  private Template template;

  @Schema(description = "页面的配置属性", type = "json")
  private T properties;
}
