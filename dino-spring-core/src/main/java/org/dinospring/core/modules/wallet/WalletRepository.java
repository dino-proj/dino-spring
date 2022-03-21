package org.dinospring.core.modules.wallet;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author tuuboo
 * @date 2022-03-09 02:22:33
 */

@Repository
public interface WalletRepository
  extends CrudRepositoryBase<WalletEntity, Long> {

  /**
   * 更新账号余额
   * @param accountId 账号id
   * @param balance 余额
   * @param oldVersion 旧版本号
   * @param newVersion 新版本号
   * @return
   */
  @Modifying
  @Query("UPDATE #{#entityName} e SET e.balance = :balance, e.version = :newVersion WHERE e.id = :accountId AND e.version = :oldVersion")
  Optional<Long> updateBalance(Long accountId, Long balance, Long oldVersion, Long newVersion);

  /**
   * 通过钱包所有者id查询钱包信息
   * @param ownerId
   * @param walletType
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.ownerId = :ownerId AND e.type = :walletType")
  Optional<WalletEntity> findByOwnerId(String ownerId, String walletType);
}
