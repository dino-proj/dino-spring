package org.dinospring.core.modules.wallet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;
import org.dinospring.data.domain.Versioned;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author tuuboo
 * @date 2022-03-01 20:54:29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "sys_wallet", indexes = {
    @Index(name = "uniq_ownerId_type", columnList = "owner_id,type", unique = true) })
public class WalletEntity extends TenantRowEntityBase<Long> implements Versioned {

  @Schema(description = "钱包Owner Id")
  @Column(name = "owner_id", nullable = false)
  private String ownerId;

  @Schema(description = "钱包类型")
  @Column(name = "type", nullable = false)
  private String type;

  @Schema(description = "钱包余额", defaultValue = "0")
  @Column(name = "balance", nullable = false)
  private Long balance = 0L;

  @Schema(description = "锁定金额", defaultValue = "0")
  @Column(name = "lock_balance", nullable = false)
  private Long lockBalance = 0L;

  @Schema(description = "版本号")
  @Column(name = "version", nullable = false)
  private Long version = 0L;

}
