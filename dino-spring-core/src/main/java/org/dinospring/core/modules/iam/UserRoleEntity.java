// Copyright 2022 dinodev.cn
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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 * 用户和角色映射关系
 * @author Cody LU
 * @date 2022-04-01 08:05:31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "iam_user_role", uniqueConstraints = @UniqueConstraint(columnNames = { "user_type", "user_id",
    "role_id" }))
public class UserRoleEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "用户类型", requiredMode = RequiredMode.REQUIRED, maxLength = 64)
  @Column(name = "user_type", length = 64, nullable = false)
  private String userType;

  @Schema(description = "用户ID", requiredMode = RequiredMode.REQUIRED, maxLength = 64)
  @Column(name = "user_id", length = 64, nullable = false)
  private String userId;

  @Schema(description = "角色ID", requiredMode = RequiredMode.REQUIRED)
  @Column(name = "role_id", nullable = false)
  private Long roleId;
}
