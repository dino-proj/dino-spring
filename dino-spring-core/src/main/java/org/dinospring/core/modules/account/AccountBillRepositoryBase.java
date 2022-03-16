package org.dinospring.core.modules.account;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author tuuboo
 * @date 2022-03-09 02:23:12
 */

@NoRepositoryBean
public interface AccountBillRepositoryBase<E extends AccountBillEntityBase> extends CrudRepositoryBase<E, Long> {

}
