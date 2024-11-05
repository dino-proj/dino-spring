// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
