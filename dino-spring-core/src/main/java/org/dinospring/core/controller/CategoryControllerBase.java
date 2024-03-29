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

package org.dinospring.core.controller;

import java.util.List;

import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.modules.category.TreeNode;
import org.dinospring.core.service.CategoryServiceBase;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;

/**
 *
 * @author JL
 */

public interface CategoryControllerBase<S extends CategoryServiceBase<N>, N extends TreeNode> {

  /**
   * Service 服务实例
   * @return
   */
  default S categoryService() {
    return ContextHelper.findBean(TypeUtils.getGenericParamClass(this, CategoryControllerBase.class, 0));
  }

  /**
   * 获取分类树
   * @param parent
   * @param keyword
   * @return
   */
  @Operation(summary = "获取分类树")
  @Parameter(name = "parent")
  @Parameter(name = "keyword")
  @GetMapping("/tree")
  @CheckPermission(":tree")
  default Response<List<N>> getCategoryTree(@Nullable Long parent, @Nullable String keyword) {

    return Response.success(this.categoryService().findCategory(parent, keyword));
  }

  /**
   * 分页获取分类树
   * @param parent
   * @param keyword
   * @param pageReq
   * @return
   */
  @Operation(summary = "分页获取分类树")
  @Parameter(name = "parent")
  @Parameter(name = "keyword")
  @GetMapping("/tree/page")
  @CheckPermission(":tree.page")
  default PageResponse<N> getCategoryTreeByPage(@Nullable Long parent,
      @Nullable String keyword, PageReq pageReq) {
    var pageable = pageReq.pageable();
    return PageResponse.success(this.categoryService().findCategory(parent, keyword, pageable));
  }

}
