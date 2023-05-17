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

package org.dinospring.core.modules.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.dinospring.core.vo.VoImplBase;

/**
 *
 * @author tuuboo
 */

@Data
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class CategoryVo extends VoImplBase<Long> {
  @Schema(description = "分类名称", maxLength = 100)
  private String name;

  @Schema(description = "分类图标", maxLength = 100)
  private String icon;

  @Schema(description = "父分类ID")
  private Long parentId;

  @Schema(description = "父分类")
  private CategoryVo parentCategory;

  @Schema(description = "是否是根节点")
  public boolean isRoot() {
    return parentId == null || parentId == 0L;
  }
}
