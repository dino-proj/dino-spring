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

package org.dinospring.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.modules.category.TreeNode;
import org.dinospring.core.service.CategoryServiceBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nullable;
import java.util.List;

/**
 *
 * @author tuuboo
 */

public interface CategoryControllerBase<S extends CategoryServiceBase<N>, N extends TreeNode> {

  default S categoryService() {
    return ContextHelper.findBean(TypeUtils.getGenericParamClass(this, CategoryControllerBase.class, 0));
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
  default Response<List<N>> getCategoryTree(@PathVariable("tenant_id") String tenantId, @Nullable Long parent,
                                            @Nullable String keyword) {
    return Response.success(categoryService().findCategory(parent, keyword));
  }

}
