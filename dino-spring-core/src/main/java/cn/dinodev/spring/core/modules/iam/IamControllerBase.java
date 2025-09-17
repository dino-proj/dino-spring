// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.iam;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.request.PageReq;
import cn.dinodev.spring.commons.request.PostBody;
import cn.dinodev.spring.commons.response.PageResponse;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.core.annotion.param.ParamPageable;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @param UK 用户主键类型
 *
 * @author Cody Lu
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
  default Response<List<ActionGroupVo>> listActionGroups(
      @Schema(name = "user_type", description = "用户类型") @RequestParam(name = "user_type", required = false) String userType) {

    return Response.success(iamService().getAllActionGroups(userType));
  }

  /**
   * 获取用户角色列表
   * @param tenant,
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
  default PageResponse<RoleVo> listUserRoles(Tenant tenant, String utype, UK uid,
      PageReq page) {
    Page<RoleVo> userRoles = iamService().listUserRoles(tenant.getId(), utype, uid.toString(), page.pageable());
    return PageResponse.success(userRoles);
  }

  /**
   * 为用户分配角色
   * @param tenant
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
  default Response<Integer> grantRoles(Tenant tenant, String utype, UK uid,
      @RequestBody PostBody<List<Long>> req) {
    var result = iamService().grantRoles(tenant.getId(), utype, uid.toString(), req.getBody());
    return Response.success(result);
  }

  /**
   * 取消用户角色
   * @param tenant,
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
  default Response<Integer> revokeRoles(Tenant tenant, String utype, UK uid,
      @RequestBody PostBody<List<Long>> req) {
    var result = iamService().revokeRoles(tenant.getId(), utype, utype, req.getBody());
    return Response.success(result);
  }

}
