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

package org.dinospring.core.modules.framework;

import org.dinospring.commons.Scope;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

/**
 * 页面布局
 * @author Cody LU
 */

@Data
public class Layout<T extends LayoutConfig> {

  @Schema(description = "布局Id")
  private Long id;

  @Schema(description = "布局标题")
  private String title;

  @Schema(description = "布局的可见范围")
  @Column(name = "access_scope", columnDefinition = "jsonb", nullable = true)
  private Scope accessScope;

  @Schema(description = "布局配置")
  @Column(name = "config", columnDefinition = "jsonb", nullable = true)
  private T config;
}
