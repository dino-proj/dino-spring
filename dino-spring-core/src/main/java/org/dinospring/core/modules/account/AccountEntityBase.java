package org.dinospring.core.modules.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.dinospring.data.domain.EntityBase;
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
public abstract class AccountEntityBase<K extends Serializable> extends EntityBase<Long> implements Versioned {

  @Schema(description = "账户Owner Id")
  @Column(name = "owner_id", nullable = false)
  private K ownerId;

  @Schema(description = "账户余额", defaultValue = "0")
  @Column(name = "balance", nullable = false)
  private Long balance = 0L;

  @Schema(description = "锁定金额", defaultValue = "0")
  @Column(name = "lock_balance", nullable = false)
  private Long lockBalance = 0L;

  @Schema(description = "版本号")
  @Column(name = "version", nullable = false)
  private Long version = 0L;

}
