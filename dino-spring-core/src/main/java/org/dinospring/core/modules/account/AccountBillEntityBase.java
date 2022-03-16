package org.dinospring.core.modules.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.dinospring.data.domain.EntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author tuuboo
 * @date 2022-03-01 01:40:42
 */

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@NoArgsConstructor
@FieldNameConstants
public abstract class AccountBillEntityBase extends EntityBase<Long> {

  @Schema(description = "账户ID")
  @Column(name = "account_id", nullable = false)
  private Long accountId;

  @Schema(description = "变动数量，正数表示增加，负数表示减少")
  @Column(name = "amount", nullable = false)
  private Long amount;

  @Schema(description = "变动后的余额")
  @Column(name = "amount", nullable = false)
  private Long balance;

  @Schema(description = "变动说明")
  @Column(name = "mark", nullable = true, length = 256)

  private String mark;

  @Schema(description = "交易类型")
  @Column(name = "order_type", nullable = true)

  private String orderType;

  @Schema(description = "订单信息")
  @Column(name = "order", nullable = true, columnDefinition = "jsonb")
  private Serializable order;

}
