// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.iam;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Cody LU
 * @date 2022-04-12 13:10:43
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "iam_resource", indexes = {
    @Index(name = "idx_iam_resource_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_iam_resource_code", columnList = "code")
})
public class PermissionEntity extends TenantRowEntityBase<Long> {
  @Column(name = "code", length = 64, nullable = false, unique = true)
  private String code;

  @Column(name = "title", length = 64, nullable = false)
  private String title;

  @Column(name = "remark", length = 255)
  private String remark;

  @Convert(disableConversion = true)
  @Column(name = "actions", columnDefinition = "jsonb")
  private List<Action> actions;
}
