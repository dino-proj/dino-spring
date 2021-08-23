package org.dinospring.core.sys.token;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantableEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Token 信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_token")
public class TokenEntity extends TenantableEntityBase<String> {

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
