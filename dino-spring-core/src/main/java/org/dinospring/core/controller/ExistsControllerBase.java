// Copyright 2022 dinodev.cn
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

import org.dinospring.auth.Operations;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.core.service.Service;
import org.dinospring.core.service.ServiceBeanResolver;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;

/**
 *
 * @author tuuboo
 * @date 2022-05-31 19:47:06
 */

public interface ExistsControllerBase<S extends Service<?, ?>, EQUERY extends CustomQuery>
    extends ServiceBeanResolver<S> {

  /**
   * 检查是否存在
   * @param tenantId 租户ID
   * @param req 查询条件
   * @return
   */
  @Operation(summary = "检查是否存在")
  @ParamTenant
  @PostMapping("exist")
  @CheckPermission(Operations.LIST)
  default Response<Boolean> exist(@PathVariable("tenant_id") String tenantId,
      @RequestBody @Validated PostBody<EQUERY> req) {
    var isExist = service().exists(req.getBody());
    return Response.success(isExist);
  }
}
