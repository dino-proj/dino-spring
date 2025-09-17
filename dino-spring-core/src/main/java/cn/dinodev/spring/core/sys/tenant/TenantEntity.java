// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.tenant;

import cn.dinodev.spring.commons.data.ImageFileMeta;
import cn.dinodev.spring.data.domain.EntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@FieldNameConstants
@Table(name = "sys_tenant")
@SequenceGenerator(name = "IdGen", sequenceName = "sys_seq_id", initialValue = 1000)
public class TenantEntity extends EntityBase<String> {

  @Schema(description = "租户名称")
  @Column(name = "name", length = 64)
  private String name;

  @Schema(description = "租户简称")
  @Column(name = "short_name", length = 16)
  private String shortName;

  @Schema(description = "租户LOGO")
  @Column(name = "icon_url", length = 2048)
  private String iconUrl;

  @Schema(description = "租户LOGO文件信息")
  @Column(name = "icon_file_meta", columnDefinition = "jsonb")
  private ImageFileMeta iconFileMeta;

  @Schema(description = "租户自定义访问域名")
  @Column(name = "custom_domain", length = 128, nullable = true, unique = true)
  String customDomain;

  @Schema(description = "租户秘钥，请不要随便显示")
  @Column(name = "secret_key", length = 256)
  String secretKey;

  @Schema(description = "租户编码", requiredMode = RequiredMode.REQUIRED, maxLength = 64)
  @Column(name = "code", length = 64, nullable = false)
  private String code;
}
