// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
