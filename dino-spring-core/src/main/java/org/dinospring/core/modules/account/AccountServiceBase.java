package org.dinospring.core.modules.account;

import java.io.Serializable;
import java.time.Duration;

import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.promise.Promise;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.utils.TaskUtils;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.entity.Code;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 17:55:44
 */

@Service
public abstract class AccountServiceBase<E extends AccountEntityBase<K>, K extends Serializable, B extends AccountBillEntityBase>
    extends ServiceBase<E, Long> {

  private final Duration nap = Duration.ofMillis(100);

  protected abstract AccountRepositoryBase<E, K> accountRepository();

  protected abstract AccountBillRepositoryBase<B> accountBillRepository();

  protected B newAccountBill() {
    var cls = TypeUtils.<B>getGenericParamClass(this, AccountServiceBase.class, 2);
    return TypeUtils.newInstance(cls);

  }

  @Override
  public CrudRepositoryBase<E, Long> repository() {
    return accountRepository();
  }

  public E getOrCreateAccountByOwner(String tenantId, K ownerId, Long change) {
    return accountRepository().findByOwnerId(ownerId).orElseGet(() -> {
      var e = newEntity();
      return accountRepository().save(e);
    });

  }

  public boolean updateBalance(String tenantId, Long accountId, Long change) {
    //最大重试次数
    Promise<Boolean> ret = TaskUtils.exec(() -> {
      var account = accountRepository().findById(accountId).orElse(null);
      Assert.notNull(account != null, Status.CODE.FAIL_NOT_FOUND);
      Assert.isTrue(Code.STATUS.OK.eq(account.getStatus()), Status.CODE.FAIL_INVALID_STAUS);

      var unlockedBalance = account.getBalance();
      Assert.isTrue(unlockedBalance >= -change, Status.fail("账户余额不足"));

      return repository().updateByIdWithVersion(accountId, AccountEntityBase.Fields.balance,
          unlockedBalance + change,
          account.getVersion());
    }, Boolean::valueOf, 5, nap, BusinessException.class);

    return ret.getOrElse(false);
  }

  public boolean lockBalance(String tenantId, Long accountId, Long amount) {
    Assert.isTrue(amount > 0L, Status.fail("锁定金额须>0"));
    //最大重试次数
    int tryTimes = 5;
    while (tryTimes > 0) {
      var account = accountRepository().findById(accountId).orElse(null);
      Assert.notNull(account != null, Status.CODE.FAIL_NOT_FOUND);
      Assert.isTrue(Code.STATUS.OK.eq(account.getStatus()), Status.CODE.FAIL_INVALID_STAUS);

      var unlockedBalance = account.getBalance();
      Assert.isTrue(unlockedBalance >= amount, Status.fail("账户余额不足"));

      var ret = repository().updateByIdWithVersion(accountId, AccountEntityBase.Fields.lockBalance,
          account.getLockBalance() + amount,
          AccountEntityBase.Fields.balance,
          unlockedBalance - amount,
          account.getVersion());
      if (ret) {
        return true;
      }
    }
    return false;
  }
}