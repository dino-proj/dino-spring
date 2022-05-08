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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamTenant;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @param UK 用户主键类型
 *
 * @author tuuboo
 * @date 2022-05-04 23:11:47
 */

public interface IamControllerBase<UK extends Serializable> {

  /**
   * 获取IamService
   * @return
   */
  default IamService iamService() {
    return ContextHelper.findBean(IamService.class);
  }

  /**
   * 获取权限组列表
   * @param userType 用户类型
   * @return
   */
  @Operation(summary = "获取权限组列表")
  @ParamTenant
  @GetMapping("/action/groups")
  @CheckPermission("sys.iam:grant")
  default Response<List<ActionGroupVo>> listActionGroups(@Schema(name = "user_type", description = "用户类型") @RequestParam(name = "user_type", required = false) String userType) {

    return Response.success(iamService().getAllActionGroups(userType));
  }

  /**
   * 获取用户角色列表
   * @param tenantId
   * @param utype 用户类型
   * @param uid 用户ID
   * @param page
   * @return
   */
  @Operation(summary = "获取用户角色列表")
  @ParamTenant
  @ParamPageable
  @GetMapping("/user/roles")
  @Parameter(name = "utype", description = "用户类型", required = true)
  @Parameter(name = "uid", description = "用户ID", required = true)
  @CheckPermission("sys.iam:grant")
  default PageResponse<RoleVo> listUserRoles(@PathVariable("tenant_id") String tenantId, String utype, UK uid,
                                             PageReq page) {
    Page<RoleVo> userRoles = iamService().listUserRoles(tenantId, utype, uid.toString(), page.pageable());
    return PageResponse.success(userRoles);
  }

  /**
   * 为用户分配角色
   * @param tenantId
   * @param utype
   * @param uid
   * @param req
   * @return
   */
  @Operation(summary = "为用户分配角色")
  @ParamTenant
  @Parameter(name = "utype", description = "用户类型", required = true)
  @Parameter(name = "uid", description = "用户ID", required = true)
  @PostMapping("/user/grant")
  @CheckPermission("sys.iam:grant")
  default Response<Long> grantRoles(@PathVariable("tenant_id") String tenantId, String utype, UK uid,
                                    @RequestBody PostBody<List<Long>> req) {
    var result = iamService().grantRoles(tenantId, utype, utype, req.getBody());
    return Response.success(result);
  }

  /**
   * 取消用户角色
   * @param tenantId
   * @param utype
   * @param uid
   * @param req
   * @return
   */
  @Operation(summary = "取消用户角色")
  @ParamTenant
  @Parameter(name = "utype", description = "用户类型", required = true)
  @Parameter(name = "uid", description = "用户ID", required = true)
  @PostMapping("/user/revoke")
  @CheckPermission("sys.iam:grant")
  default Response<Long> revokeRoles(@PathVariable("tenant_id") String tenantId, String utype, UK uid,
                                     @RequestBody PostBody<List<Long>> req) {
    var result = iamService().revokeRoles(tenantId, utype, utype, req.getBody());
    return Response.success(result);
  }

}
