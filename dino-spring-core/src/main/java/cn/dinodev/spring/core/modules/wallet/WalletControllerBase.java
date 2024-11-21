package cn.dinodev.spring.core.modules.wallet;

import org.springframework.web.bind.annotation.GetMapping;

import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.request.PageReq;
import cn.dinodev.spring.commons.response.PageResponse;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.core.annotion.param.ParamPageable;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import cn.dinodev.spring.core.controller.ControllerBase;
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
   * @param tenant
   * @return
   */
  @Operation(summary = "获取登录账户信息")
  @ParamTenant
  @GetMapping("/info")
  @CheckPermission(":wallet.info")
  default Response<WalletVo> getSummary(Tenant tenant) {
    var wallet = walletService().getOrCreateAccountByOwner(tenant.getId(),
        ContextHelper.currentUser().getId().toString(),
        walletType());

    return Response.success(walletService().projection(WalletVo.class, wallet));
  }

  /**
   * 获取流水
   * @param tenant
   * @param pageReq
   * @return
   */
  @Operation(summary = "获取登录账户流水")
  @ParamTenant
  @ParamPageable
  @GetMapping("/bills")
  @CheckPermission(":wallet.bills")
  default PageResponse<WalletBillVo> getBills(Tenant tenant, PageReq pageReq) {
    var wallet = walletService().getOrCreateAccountByOwner(tenant.getId(),
        ContextHelper.currentUser().getId().toString(),
        walletType());
    return PageResponse
        .success(walletService().listBills(tenant.getId(), wallet.getId(), pageReq.pageable(), WalletBillVo.class));
  }
}
