// Copyright 2022 dinospring.cn
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
package org.dinospring.core.modules.iam;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.controller.CrudControllerBase;
import org.dinospring.core.modules.iam.RoleControllerBase.RoleReq;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 * @date 2022-05-04 23:43:44
 */

public interface RoleControllerBase
    extends CrudControllerBase<RoleService, RoleEntity, RoleVo, RoleSearch, RoleReq, Long> {

  /**
   * 为角色赋权
   * @param tenantId
   * @param req
   * @param id
   * @return
   */
  @Operation(summary = "设置角色权限")
  @ParamTenant
  @PostMapping("/grant")
  default Response<RoleVo> post(@PathVariable("tenant_id") String tenantId,
      @RequestBody PostBody<List<String>> req, Long id) {

    return Response.success(null);
  }

  @Data
  public class RoleReq {
    @Schema(description = "角色编码", required = true, maxLength = 64)
    @NotBlank
    @Size(max = 64)
    private String code;

    @Schema(description = "角色名称", required = true, maxLength = 64)
    @NotBlank
    @Size(max = 64)
    private String title;

    @Schema(description = "角色备注", required = false, maxLength = 255)
    @Nullable
    @Size(max = 255)
    private String remark;
  }

}
