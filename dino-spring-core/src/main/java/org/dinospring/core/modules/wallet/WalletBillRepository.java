package org.dinospring.core.modules.wallet;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Cody LU
 * @date 2022-03-09 02:23:12
 */

@Repository
public interface WalletBillRepository extends CrudRepositoryBase<WalletBillEntity, Long> {

}
