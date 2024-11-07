package cn.dinodev.spring.core.modules.wallet;

import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.request.PageReq;
import cn.dinodev.spring.commons.response.PageResponse;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.core.annotion.param.ParamPageable;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import cn.dinodev.spring.core.controller.ControllerBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Cody Lu
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
  @Operation(summary = "获取登录账户信息")
  @ParamTenant
  @GetMapping("/info")
  @CheckPermission(":wallet.info")
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
  @Operation(summary = "获取登录账户流水")
  @ParamTenant
  @ParamPageable
  @GetMapping("/bills")
  @CheckPermission(":wallet.bills")
  default PageResponse<WalletBillVo> getBills(@PathVariable("tenant_id") String tenantId, PageReq pageReq) {
    var wallet = walletService().getOrCreateAccountByOwner(tenantId, ContextHelper.currentUser().getId().toString(),
        walletType());
    return PageResponse
        .success(walletService().listBills(tenantId, wallet.getId(), pageReq.pageable(), WalletBillVo.class));
  }
}
