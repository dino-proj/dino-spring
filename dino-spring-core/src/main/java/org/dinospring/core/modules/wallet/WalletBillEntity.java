package org.dinospring.core.modules.wallet;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.dinospring.data.domain.TenantRowEntityBase;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author tuuboo
 * @date 2022-03-01 01:40:42
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "sys_wallet_bills", indexes = {
    @Index(name = "idx_accountId", columnList = "account_id", unique = false) })
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
public abstract class WalletBillEntity extends TenantRowEntityBase<Long> {

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

  @Type(type = "json")
  @Convert(disableConversion = true)
  @Schema(description = "订单信息")
  @Column(name = "[order]", nullable = true, columnDefinition = "jsonb")
  private Serializable order;

}
