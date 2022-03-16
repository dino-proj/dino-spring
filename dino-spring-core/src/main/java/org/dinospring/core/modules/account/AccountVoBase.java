package org.dinospring.core.modules.account;

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
public class AccountVoBase extends VoImplBase<Long> {

  @Schema(description = "余额")
  private Long balance;

}
