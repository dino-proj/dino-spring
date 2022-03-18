package org.dinospring.core.modules.wallet;

import org.dinospring.core.vo.VoImplBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author tuuboo
 * @date 2022-03-01 01:35:43
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WalletVo extends VoImplBase<Long> {

  @Schema(description = "账户Owner Id")
  private String ownerId;

  @Schema(description = "钱包类型")
  private String type;

  @Schema(description = "余额")
  private Long balance;

  @Schema(description = "锁定金额", defaultValue = "0")
  private Long lockBalance = 0L;
}
