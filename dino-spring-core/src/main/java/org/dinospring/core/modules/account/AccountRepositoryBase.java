package org.dinospring.core.modules.account;

import java.io.Serializable;
import java.util.Optional;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author tuuboo
 * @date 2022-03-09 02:22:33
 */

@NoRepositoryBean
public interface AccountRepositoryBase<E extends AccountEntityBase<K>, K extends Serializable>
    extends CrudRepositoryBase<E, Long> {

  @Modifying
  @Query("UPDATE #{#entityName} e SET e.balance = :balance, e.ver = :newVersion WHERE e.id = :accountId AND e.ver = :oldVersion")
  Optional<Long> updateBalance(Long accountId, Long balance, Long oldVersion, Long newVersion);

  @Query("FROM #{#entityName} e WHERE e.ownerId = :ownerId")
  Optional<E> findByOwnerId(K ownerId);
}
