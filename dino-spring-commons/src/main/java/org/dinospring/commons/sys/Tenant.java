package org.dinospring.commons.sys;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

public interface Tenant extends Serializable {
  @Schema(description = "租户ID")
  String getId();

  @Schema(description = "租户名称")
  String getName();

  @Schema(description = "租户简称")
  String getShortName();

  @Schema(description = "租户LOGO")
  String getIconUrl();

  @Schema(description = "租户子域名, 用于PC和H5端")
  String getSubDomain();

  @Schema(description = "租户自定义域名,如果用户配置了域名，则用其自己的域名")
  String getCustomDomain();

  @Schema(description = "租户的SecretKey", hidden = true)
  @JsonIgnore
  String getSecretKey();

  @Schema(description = "租户状态")
  Integer getStatus();

  static boolean isSys(String tenantId) {
    return "SYS".equals(tenantId);
  }
}
