package org.dinospring.core.modules.wallet;

import java.io.Serializable;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody Lu
 * @date 2022-03-01 01:40:42
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "sys_wallet_bills", indexes = {
    @Index(name = "idx_accountId", columnList = "account_id", unique = false) })
public class WalletBillEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "账户ID")
  @Column(name = "account_id", nullable = false)
  private Long accountId;

  @Schema(description = "变动数量，正数表示增加，负数表示减少")
  @Column(name = "amount", nullable = false)
  private Long amount;

  @Schema(description = "是否为冻结")
  @Column(name = "is_lock", nullable = false)
  private Boolean isLock;

  @Schema(description = "变动后的余额")
  @Column(name = "balance", nullable = false)
  private Long balance;

  @Schema(description = "变动说明")
  @Column(name = "mark", nullable = true, length = 256)
  private String mark;

  @Schema(description = "交易类型")
  @Column(name = "order_type", nullable = true)
  private String orderType;

  @Schema(description = "订单信息")
  @Column(name = "[order]", nullable = true, columnDefinition = "jsonb")
  private Serializable order;

}
