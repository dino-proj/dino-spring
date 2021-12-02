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
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.controller.CategoryControllerBase;
import org.dinospring.core.controller.CrudControllerBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author tuuboo
 * @author JL
 */

public interface CategoryController<S extends CategoryService<E, N>, E extends CategoryEntityBase, VO extends CategoryVo, SRC extends CategorySearch, REQ extends CategoryController.CategoryReq, N extends TreeNode> extends CrudControllerBase<S, E, VO, SRC, REQ, Long>, CategoryControllerBase<S, N> {

  /**
   * 服务实例
   * @return
   */
  @Override
  default S categoryService() {
    return service();
  }

  /**
   * 服务实例
   * @return
   */
  @Override
  default S service() {
    return ContextHelper.findBean(TypeUtils.getGenericParamClass(this, CategoryController.class, 0));
  }

  /**
   * Entity类的Class
   * @return
   */
  @Override
  @Nonnull
  default Class<E> entityClass() {
    return TypeUtils.getGenericParamClass(this, CategoryController.class, 1);
  }

  /**
   * Vo类的Class
   * @return
   */
  @Override
  @Nonnull
  default Class<VO> voClass() {
    return TypeUtils.getGenericParamClass(this, CategoryController.class, 2);
  }

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
