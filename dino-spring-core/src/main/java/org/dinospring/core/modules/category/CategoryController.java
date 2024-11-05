// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.controller.CategoryControllerBase;
import org.dinospring.core.controller.CrudControllerBase;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Cody LU
 * @author JL
 */

public interface CategoryController<S extends CategoryService<E, N>, E extends CategoryEntityBase, VO extends CategoryVo, SRC extends CategorySearch, REQ extends CategoryController.CategoryReq, N extends TreeNode>
    extends CrudControllerBase<S, E, VO, SRC, REQ, Long>, CategoryControllerBase<S, N> {

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
