// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller;

import cn.dinodev.spring.auth.Operations;
import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.commons.request.PostBody;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.core.service.CustomQuery;
import cn.dinodev.spring.core.service.Service;
import cn.dinodev.spring.core.service.ServiceBeanResolver;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;

/**
 *
 * @author Cody Lu
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
