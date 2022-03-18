package org.dinospring.core.modules.wallet;

import java.io.Serializable;

import org.dinospring.core.vo.VoImplBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author tuuboo
 * @date 2022-03-01 01:40:42
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WalletBillVo extends VoImplBase<Long> {

  @Schema(description = "账户ID")
  private Long accountId;

  @Schema(description = "变动数量，正数表示增加，负数表示减少")
  private Long amount;

  @Schema(description = "是否为冻结")
  private Boolean isLock;

  @Schema(description = "变动后的余额")
  private Long balance;

  @Schema(description = "变动说明")
  private String mark;

  @Schema(description = "交易类型")
  private String orderType;

  @Schema(description = "订单信息")
  private Serializable order;

}
