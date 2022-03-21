package org.dinospring.core.modules.wallet;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.controller.ControllerBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author tuuboo
 */
public interface WalletControllerBase
    extends ControllerBase<WalletService, WalletEntity, WalletVo, Long> {

  /**
   * 钱包类型
   * @return
   */
  WalletType walletType();

  /**
   * 业务处理类
   * @return
   */
  WalletService walletService();

  /**
   * 获取账户信息
   * @param tenantId
   * @return
   */
  @Operation(summary = "获取账户信息")
  @ParamTenant
  @GetMapping("/info")
  default Response<WalletVo> getSummary(@PathVariable("tenant_id") String tenantId) {
    var wallet = walletService().getOrCreateAccountByOwner(tenantId, ContextHelper.currentUser().getId().toString(),
        walletType());

    return Response.success(walletService().projection(WalletVo.class, wallet));
  }

  /**
   * 获取流水
   * @param tenantId
   * @param pageReq
   * @return
   */
  @Operation(summary = "获取流水")
  @ParamTenant
  @ParamPageable
  @GetMapping("/bills")
  default PageResponse<WalletBillVo> getBills(@PathVariable("tenant_id") String tenantId, PageReq pageReq) {
    var wallet = walletService().getOrCreateAccountByOwner(tenantId, ContextHelper.currentUser().getId().toString(),
        walletType());
    return PageResponse
        .success(walletService().listBills(tenantId, wallet.getId(), pageReq.pageable(), WalletBillVo.class));
  }
}
