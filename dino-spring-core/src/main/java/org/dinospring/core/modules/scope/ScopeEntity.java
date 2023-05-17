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

package org.dinospring.core.modules.scope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author tuuboo
 * @date 2022-03-31 16:50:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "sys_scope", indexes = { @Index(name = "idx_rule_hash", columnList = "rule_hash") })
public class ScopeEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "订单信息")
  @Column(name = "scope_rule", nullable = true, columnDefinition = "jsonb")
  private ScopeRule scopeRule;

  @Schema(description = "规则的Hash值，用于做规则唯一性判断")
  @Column(name = "rule_hash", length = 256, nullable = false, unique = true)
  private String ruleHash;
}
