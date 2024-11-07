// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.core.modules.category;

import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author Cody Lu
 */

@NoRepositoryBean
public interface CategoryRepository<E extends CategoryEntityBase> extends CrudRepositoryBase<E, Long> {

}
