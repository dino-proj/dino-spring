// Copyright 2021 dinodev.cn
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

package org.dinospring.core.sys.tenant;

import org.dinospring.commons.data.ImageFileMeta;
import org.dinospring.data.domain.EntityBase;

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
 * @author Cody LU
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
