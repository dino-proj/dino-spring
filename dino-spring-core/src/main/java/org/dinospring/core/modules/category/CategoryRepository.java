// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package org.dinospring.core.modules.category;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author Cody LU
 */

@NoRepositoryBean
public interface CategoryRepository<E extends CategoryEntityBase> extends CrudRepositoryBase<E, Long> {

}
