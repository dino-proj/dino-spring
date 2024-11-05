// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.iam;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 * 角色实体
 * @author Cody LU
 * @date 2022-04-01 08:05:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "iam_role")
public class RoleEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "角色编码", requiredMode = RequiredMode.REQUIRED, maxLength = 64)
  @Column(name = "code", length = 64, nullable = false, updatable = false)
  private String code;

  @Schema(description = "角色名称", requiredMode = RequiredMode.REQUIRED, maxLength = 64)
  @Column(name = "name", length = 64, nullable = false)
  private String name;

  @Schema(description = "角色备注", requiredMode = RequiredMode.NOT_REQUIRED, maxLength = 255)
  @Column(name = "remark", length = 255)
  private String remark;

  @Schema(description = "角色操作权限", requiredMode = RequiredMode.NOT_REQUIRED)
  @Column(name = "permissions", columnDefinition = "jsonb")
  private List<String> permissions;

  @Schema(description = "角色数据权限", requiredMode = RequiredMode.NOT_REQUIRED)
  @Column(name = "data_permissions", columnDefinition = "jsonb")
  private List<String> dataPermissions;

  @Schema(description = "角色菜单权限", requiredMode = RequiredMode.NOT_REQUIRED)
  @Column(name = "menu_permissions", columnDefinition = "jsonb")
  private List<String> menuPermissions;
}
