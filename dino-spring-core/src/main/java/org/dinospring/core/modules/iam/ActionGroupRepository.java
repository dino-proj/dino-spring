// Copyright 2022 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.iam;

import java.util.List;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Cody LU
 * @date 2022-05-04 23:19:34
 */

@Repository
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
