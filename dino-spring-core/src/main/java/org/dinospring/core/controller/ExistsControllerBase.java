// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller;

import org.dinospring.auth.Operations;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.core.service.Service;
import org.dinospring.core.service.ServiceBeanResolver;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;

/**
 *
 * @author Cody LU
 * @date 2022-05-31 19:47:06
 */

public interface ExistsControllerBase<S extends Service<?, ?>, EQUERY extends CustomQuery>
    extends ServiceBeanResolver<S> {

  /**
   * 检查是否存在
   * @param req 查询条件
   * @return
   */
  @Operation(summary = "检查是否存在")
  @PostMapping("exist")
  @CheckPermission(Operations.LIST)
  default Response<Boolean> exist(@RequestBody @Validated PostBody<EQUERY> req) {
    var isExist = this.service().exists(req.getBody());
    return Response.success(isExist);
  }
}
