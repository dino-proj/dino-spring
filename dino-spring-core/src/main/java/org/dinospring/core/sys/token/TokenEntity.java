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
