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

import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.botbrain.dino.sql.builder.SelectSqlBuilder;

import org.dinospring.commons.response.Response;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.controller.CrudControllerBase;
import org.dinospring.core.controller.support.StatusQuery;
import org.dinospring.core.modules.category.CategoryController.CategroyReq;
import org.dinospring.core.modules.category.CategoryController.CategroySearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author tuuboo
 */

public interface CategoryController<S extends CategoryService<E>, E extends CategoryEntityBase, VO extends CategoryVo>
    extends CrudControllerBase<S, E, VO, CategroySearch, CategroyReq, Long> {

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
  @EqualsAndHashCode(callSuper = true)
  public static class CategroySearch extends StatusQuery {

    @Schema(description = "根据名字模糊查询")
    private String name;

    @Schema(description = "根据父分类ID筛选")
    private Long parentId;

    @Override
    public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
      sql.eqIfNotNull("parent_id", parentId);
      sql.like(CategoryEntityBase.Fields.name, this.name);
      return super.buildSql(sql);
    }

  }

  @Data
  public static class CategroyReq {
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
