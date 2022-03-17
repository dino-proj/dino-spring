package org.dinospring.core.sys.tenant;

import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.controller.ControllerBase;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface TenantControllerBase extends ControllerBase<TenantService, TenantEntity, TenantVo, String> {

  /**
   * 根据域名查询Tenant
   * @param domain 域名
   * @return
   */
  @Operation(summary = "根据域名查询Tenant")
  @Parameter(name = "domain", description = "域名信息")
  @GetMapping("/bydomian")
  default Response<TenantVo> getByDomain(String domain) {

    Assert.hasText(domain, Status.CODE.FAIL_INVALID_PARAM.withMsg("domain 不能为空"));
    return Response.success(service().findTenantByDomain(domain, TenantVo.class));
  }

  /**
   * 根据域名查询Tenant
   * @param domain 域名
   * @return
   */
  @Operation(summary = "根据域名查询Tenant")
  @Parameter(name = "id", description = "Tenant Id")
  @GetMapping("/byid")
  default Response<TenantVo> getById(String id) {

    Assert.hasText(id, Status.CODE.FAIL_INVALID_PARAM.withMsg("id 不能为空"));
    return Response.success(service().findTenantById(id, TenantVo.class));
  }
}
