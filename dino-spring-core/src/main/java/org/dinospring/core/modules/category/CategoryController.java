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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.controller.CrudControllerBase;
import org.dinospring.core.service.CustomQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author tuuboo
 */

public interface CategoryController<S extends CategoryService<E>, E extends CategoryEntityBase, VO extends CategoryVo, SRC extends CustomQuery>
  extends CrudControllerBase<S, E, VO, SRC, CategoryController.CategoryReq, Long> {

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

  /**
   * 为VO添加parentCategory信息
   * @param vo
   * @return
   */
  @Override
  default VO processVo(VO vo) {
    if (vo.isRoot()) {
      vo.setParentCategory(service().getById(vo.getParentId(), voClass()));
    }
    return vo;
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

    @Schema(description = "父分类ID，不传则表示一级分类")
    @Nullable
    private Long parentId;
  }

  /**
   * 获取分类树
   * @param tenantId
   * @param parent
   * @param keyword
   * @return
   */
  @Operation(summary = "获取分类树")
  @ParamTenant
  @Parameter(name = "parent")
  @Parameter(name = "keyword")
  @GetMapping("/tree")
  default Response<List<TreeNode>> getCategoryTree(@PathVariable("tenant_id") String tenantId, @Nullable Long parent,
                                                   @Nullable String keyword) {
    return Response.success(service().findCategory(parent, keyword));
  }
}
