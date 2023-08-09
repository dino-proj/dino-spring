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

package org.dinospring.core.modules.framework.template;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.dinospring.core.modules.framework.PageType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody LU
 */

@Data
public class Template {

  @Schema(description = "模板ID")
  private String name;

  @Schema(description = "模板标题")
  private String title;

  @Schema(description = "模板图标")
  private String icon;

  @Schema(description = "预览图片")
  private String previewImage;

  @Schema(description = "页面APP端路径")
  private String appPath;

  @Schema(description = "页面pc端路径")
  private String pcPath;

  @Schema(description = "对模板的描述")
  private String description;

  @Schema(description = "页面类型")
  private PageType type;

  @JsonIgnore
  private Class<?> confClass;
}
