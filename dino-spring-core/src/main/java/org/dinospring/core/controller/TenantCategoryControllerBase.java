// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller;

import java.util.List;

import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.modules.category.TreeNode;
import org.dinospring.core.service.CategoryServiceBase;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;

/**
 *
 * @author Cody Lu
 * @date 2023-11-16 20:03:14
 */

public interface TenantCategoryControllerBase<S extends CategoryServiceBase<N>, N extends TreeNode> {

  /**
   * Service 服务实例
   * @return
   */
  default S categoryService() {
    return ContextHelper.findBean(TypeUtils.getGenericParamClass(this, TenantCategoryControllerBase.class, 0));
  }

  /**
   * 获取分类树结构
   * @param tenant 租户
   * @param parent 父节点Id，null表示根节点
   * @param keyword 关键字，根据名称模糊查询
   * @return
   */
  @Operation(summary = "获取分类树")
  @ParamTenant
  @Parameter(name = "parent")
  @Parameter(name = "keyword")
  @GetMapping("/tree")
  @CheckPermission(":tree")
  default Response<List<N>> getCategoryTree(Tenant tenant, @Nullable Long parent,
      @Nullable String keyword) {
    return Response.success(this.categoryService().findCategory(parent, keyword));
  }

  /**
   * 分页获取分类树
   * @param tenant
   * @param parent
   * @param keyword
   * @param pageReq
   * @return
   */
  @Operation(summary = "分页获取分类树")
  @ParamTenant
  @Parameter(name = "parent")
  @Parameter(name = "keyword")
  @GetMapping("/tree/page")
  @CheckPermission(":tree.page")
  default PageResponse<N> getCategoryTreeByPage(Tenant tenant, @Nullable Long parent,
      @Nullable String keyword, PageReq pageReq) {
    var pageable = pageReq.pageable();
    return PageResponse.success(this.categoryService().findCategory(parent, keyword, pageable));
  }

}
