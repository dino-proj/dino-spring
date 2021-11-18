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

package org.dinospring.core.modules.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dinospring.core.controller.CategoryControllerBase;
import org.dinospring.core.controller.CrudControllerBase;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author tuuboo
 */

public interface CategoryController<S extends CategoryService<E>, E extends CategoryEntityBase, VO extends CategoryVo> extends CrudControllerBase<S, E, VO, CategorySearch, CategoryController.CategoryReq, Long>, CategoryControllerBase<S, TreeNode> {


  @Data
  public static class CategoryReq {
    @Schema(description = "分类名字")
    @Size(max = 100)
    @NotBlank
    private String name;

    @Schema(description = "分类的图标")
    @Nullable
    private String icon;

    @Schema(name = "parent_id", description = "父分类ID，不传则表示一级分类")
    @Nullable
    private Long parentId;
  }

}
