package org.dinospring.core.modules.wallet;

import java.util.Optional;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody Lu
 * @date 2022-03-09 02:22:33
 */

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
  @Query("UPDATE #{#entityName} e SET e.balance = :balance, e.version = e.version + 1 WHERE e.id = :accountId AND e.version = :oldVersion")
  int updateBalance(Long accountId, Long balance, Long oldVersion);

  /**
   * 通过钱包所有者id查询钱包信息
   * @param ownerId
   * @param walletType
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.ownerId = :ownerId AND e.type = :walletType")
  Optional<WalletEntity> findByOwnerId(String ownerId, String walletType);
}
