// Copyright 2021 dinospring.cn
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.data.domain.EntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author tuuboo
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@FieldNameConstants
@Table(name = "sys_tenant")
public class TenantEntity extends EntityBase<String> {

  @Schema(description = "租户名称")
  @Column(name = "name", length = 64)
  String name;

  @Schema(description = "租户简称")
  @Column(name = "short_name", length = 16)
  String shortName;

  @Schema(description = "租户LOGO")
  @Column(name = "icon_url", length = 2048)
  String iconUrl;

  @Schema(description = "租户子域名，用于PC和H5端")
  @Column(name = "sub_domain", length = 16)
  String subDomain;

  @Schema(description = "租户自定义域名，如果用户配置了域名，则用其自己的域名")
  @Column(name = "custom_domain", length = 128)
  String customDomain;

  @Schema(description = "租户秘钥，请不要随便显示")
  @Column(name = "secret_key", length = 256)
  String secretKey;
}
