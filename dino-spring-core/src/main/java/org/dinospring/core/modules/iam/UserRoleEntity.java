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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户和角色映射关系
 * @author tuuboo
 * @date 2022-04-01 08:05:31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "iam_user_role")
public class UserRoleEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "用户类型", required = true, maxLength = 64)
  @Column(name = "user_type", length = 64, nullable = false)
  private String userType;

  @Schema(description = "用户ID", required = true, maxLength = 64)
  @Column(name = "user_id", length = 64, nullable = false)
  private String userId;

  @Schema(description = "角色ID", required = true)
  @Column(name = "role_id", nullable = false)
  private Long roleId;
}
