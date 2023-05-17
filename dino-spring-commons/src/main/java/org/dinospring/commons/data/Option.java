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

package org.dinospring.commons.data;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data
public class Option<V extends Serializable> implements ValueLabel<V> {

  @Schema(description = "选项值", required = true)
  private V value;

  @Schema(description = "选项标签", required = true)
  private String label;

  @Schema(description = "选项的图标")
  private String icon;

  @Schema(description = "选项的样式")
  private String style;

}
