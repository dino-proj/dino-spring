// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.token;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 * Token 信息
 *
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@FieldNameConstants
@Table(name = "sys_token")
public class TokenEntity extends TenantRowEntityBase<String> {

  @Schema(description = "Token标签，用于区分不同的Token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Column(length = 128, nullable = true)
  private String label;

  @Schema(description = "用户ID")
  @Column(length = 64)
  private String userId;

  @Schema(description = "用户类型")
  @Column(length = 16)
  private String userType;

  @Schema(description = "Token串")
  @Column(length = 128)
  private String token;

  @Schema(description = "刷新Token串")
  @Column(name = "refresh_token", length = 128)
  private String refreshToken;

  @Schema(description = "Token过期时间，单位秒")
  @Column(name = "expires_in")
  private Long expiresIn;

  @Schema(description = "RefreshToken过期时间，单位秒")
  @Column(name = "refresh_expires_in")
  private Long refreshExpiresIn;
}
