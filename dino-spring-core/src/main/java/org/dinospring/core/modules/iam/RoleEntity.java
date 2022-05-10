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

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.dinospring.data.domain.TenantRowEntityBase;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * 角色实体
 * @author tuuboo
 * @date 2022-04-01 08:05:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "iam_role")
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
public class RoleEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "角色编码", required = true, maxLength = 64)
  @Column(name = "code", length = 64, nullable = false, updatable = false)
  private String code;

  @Schema(description = "角色名称", required = true, maxLength = 64)
  @Column(name = "name", length = 64, nullable = false)
  private String name;

  @Schema(description = "角色备注", required = false, maxLength = 255)
  @Column(name = "remark", length = 255)
  private String remark;

  @Type(type = "json")
  @Convert(disableConversion = true)
  @Schema(description = "角色操作权限", required = false)
  @Column(name = "permissions", columnDefinition = "jsonb")
  private List<String> permissions;

  @Type(type = "json")
  @Convert(disableConversion = true)
  @Schema(description = "角色数据权限", required = false)
  @Column(name = "data_permissions", columnDefinition = "jsonb")
  private List<String> dataPermissions;

  @Type(type = "json")
  @Convert(disableConversion = true)
  @Schema(description = "角色菜单权限", required = false)
  @Column(name = "menu_permissions", columnDefinition = "jsonb")
  private List<String> menuPermissions;
}
