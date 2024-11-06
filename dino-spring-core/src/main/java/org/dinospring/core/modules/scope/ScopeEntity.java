// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.scope;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Cody Lu
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
