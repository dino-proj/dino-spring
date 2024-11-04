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

package org.dinospring.data.domain;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 * 基于Row的Tenant实体基类，
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
public abstract class TenantRowEntityBase<K extends Serializable> extends EntityBase<K> implements TenantRowEntity {

  /**
  * 租户ID
  */
  @Schema(description = "租户ID")
  @Column(name = "tenant_id", nullable = true, updatable = false, length = 16)
  private String tenantId;
}
