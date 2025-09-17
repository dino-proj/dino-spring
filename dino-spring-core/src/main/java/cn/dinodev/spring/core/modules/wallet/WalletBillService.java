package cn.dinodev.spring.core.modules.wallet;

import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Jack Liu
 * @Date: 2022/6/16 16:23
 */
@Service
public class WalletBillService extends ServiceBase<WalletBillEntity, Long> {

  @Autowired
  private WalletBillRepository walletBillRepository;

  @Override
  public CrudRepositoryBase<WalletBillEntity, Long> repository() {
    return walletBillRepository;
  }
}
