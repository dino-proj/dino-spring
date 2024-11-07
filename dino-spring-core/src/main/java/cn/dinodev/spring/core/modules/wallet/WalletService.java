package cn.dinodev.spring.core.modules.wallet;

import cn.dinodev.spring.commons.exception.BusinessException;
import cn.dinodev.spring.commons.promise.Promise;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.commons.utils.TaskUtils;
import cn.dinodev.spring.core.entity.Code;
import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 17:55:44
 */

@Service
public class WalletService
    extends ServiceBase<WalletEntity, Long> {

  private final Duration nap = Duration.ofMillis(100);

  @Autowired
  private WalletRepository walletRepository;

  @Autowired
  private WalletBillRepository walletBillRepository;

  @Override
  public CrudRepositoryBase<WalletEntity, Long> repository() {
    return walletRepository;
  }

  public WalletEntity getOrCreateAccountByOwner(String tenantId, String ownerId, WalletType walletType) {
    return walletRepository.findByOwnerId(ownerId, walletType.getName()).orElseGet(() -> {
      var e = newEntity();
      e.setOwnerId(ownerId);
      e.setType(walletType.getName());
      e.setTenantId(tenantId);
      return this.save(e);
    });

  }

  public Page<WalletBillEntity> listBills(String tenantId, Long accountId, Pageable page) {
    return listBills(tenantId, accountId, page, WalletBillEntity.class);
  }

  public <T> Page<T> listBills(String tenantId, Long accountId, Pageable page, Class<T> cls) {
    var sql = walletBillRepository.newSelect();
    sql.eq("account_id", accountId);
    sql.orderBy("create_at", false);
    return walletBillRepository.queryPage(sql, page, cls);
  }

  @Transactional(rollbackFor = Exception.class)
  public boolean updateBalance(String tenantId, Long accountId, Long change, WalletBillEntity bill) {
    //最大重试5次
    Promise<Boolean> ret = TaskUtils.exec(() -> {
      var account = walletRepository.findById(accountId).orElse(null);
      Assert.notNull(account != null, Status.CODE.FAIL_NOT_FOUND);
      Assert.isTrue(Code.STATUS.OK.eq(account.getStatus()), Status.CODE.FAIL_INVALID_STAUS);

      var newBalance = account.getBalance() + change;
      Assert.isTrue(newBalance >= 0, Status.fail("账户余额不足"));

      Map<String, Object> map;
      if (change < 0) {
        map = Map.of(WalletEntity.Fields.balance, newBalance, WalletEntity.Fields.disburse,
            account.getDisburse() - change);
      } else {
        map = Map.of(WalletEntity.Fields.balance, newBalance);
      }
      if (repository().updateByIdWithVersion(accountId, map,
          account.getVersion())) {
        bill.setIsLock(false);
        bill.setAccountId(accountId);
        bill.setAmount(change);
        bill.setBalance(newBalance);

        walletBillRepository.save(bill);
        return true;
      } else {
        return false;
      }
    }, Boolean::valueOf, 5, nap, BusinessException.class);

    return ret.getOrElse(false);
  }

  @Transactional(rollbackFor = Exception.class)
  public boolean lockBalance(String tenantId, Long accountId, Long amount, WalletBillEntity bill) {
    Assert.isTrue(amount > 0L, Status.fail("锁定金额须>0"));
    //最大重试5次
    Promise<Boolean> ret = TaskUtils.exec(() -> {
      var account = walletRepository.findById(accountId).orElse(null);
      Assert.notNull(account != null, Status.CODE.FAIL_NOT_FOUND);
      Assert.isTrue(Code.STATUS.OK.eq(account.getStatus()), Status.CODE.FAIL_INVALID_STAUS);

      var newBalance = account.getBalance() - amount;
      Assert.isTrue(newBalance >= 0L, Status.fail("账户余额不足"));

      if (repository().updateByIdWithVersion(accountId, WalletEntity.Fields.lockBalance,
          account.getLockBalance() + amount,
          WalletEntity.Fields.balance,
          newBalance,
          account.getVersion())) {
        bill.setIsLock(true);
        bill.setAccountId(accountId);
        bill.setAmount(-1 * amount);
        bill.setBalance(newBalance);
        walletBillRepository.save(bill);
        return true;
      } else {
        return false;
      }
    }, Boolean::valueOf, 5, nap, BusinessException.class);
    return ret.getOrElse(false);
  }

}