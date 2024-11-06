// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.iam;

import java.util.List;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody Lu
 * @date 2022-05-04 23:19:34
 */

public interface ActionGroupRepository extends CrudRepositoryBase<ActionGroupEntity, Long> {

  /**
   * 通过用户类型查询所有权限
   * @param userType
   * @param class1
   * @return
   */
  @Query("FROM ActionGroupEntity a where a.userType is null or a.userType = :userType")
  List<ActionGroupVo> findAllByUserType(String userType, Class<ActionGroupVo> class1);

}
