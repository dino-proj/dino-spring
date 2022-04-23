package org.dinospring.core.sys.tenant;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.controller.ControllerBase;
import org.dinospring.core.modules.oss.OssService;
import org.dinospring.core.utils.MultiMediaUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * @author tuuboo
 */
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
   * @param id ID
   * @return
   */
  @Operation(summary = "根据域名查询Tenant")
  @Parameter(name = "id", description = "Tenant Id")
  @GetMapping("/byid")
  default Response<TenantVo> getById(String id) {

    Assert.hasText(id, Status.CODE.FAIL_INVALID_PARAM.withMsg("id 不能为空"));
    return Response.success(service().findTenantById(id, TenantVo.class));
  }

  /**
   * 根据域名查询favorit.ico
   * @param domain
   * @param response
   * @throws IOException
   */
  @Operation(summary = "根据域名查询favorit.ico")
  @Parameter(name = "domain", description = "域名")
  @GetMapping(value = "/favicon.ico")
  default void getFavicon(String domain, HttpServletResponse response) throws IOException {
    var tenant = service().findTenantByDomain(domain, TenantEntity.class);

    Assert.notNull(tenant, Status.CODE.FAIL_INVALID_PARAM.withMsg("domain 不存在"));

    response.addHeader(HttpHeaders.CONTENT_TYPE, "image/x-icon");
    var ossService = ContextHelper.findBean(OssService.class);

    var image = ImageIO
        .read(ossService.getObject(tenant.getIconFileMeta().getBucket(), tenant.getIconFileMeta().getPath()));
    MultiMediaUtils.writeIcoImage(image, response.getOutputStream(), 32);

  }
}
