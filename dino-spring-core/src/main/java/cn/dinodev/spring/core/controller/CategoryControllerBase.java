// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller;

import java.util.List;

import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.request.PageReq;
import cn.dinodev.spring.commons.response.PageResponse;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.core.modules.category.TreeNode;
import cn.dinodev.spring.core.service.CategoryServiceBase;
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
