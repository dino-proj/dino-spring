// Copyright 2021 dinodev.cn
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

package org.dinospring.core.modules.framework;

import java.util.Optional;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author tuuboo
 */

public interface PageRepository extends CrudRepositoryBase<PageEntity, Long> {

  /**
   * 根据模板查询页面
   * @param tenantId
   * @param templateName
   * @return
   */
  @Query("FROM PageEntity p WHERE p.tenantId = :tenantId AND p.templateName = :templateName")
  Optional<PageEntity> getOneByTemplateName(String tenantId, String templateName);

  /**
   * 根据id查询页面
   * @param tenantId
   * @param id
   * @return
   */
  @Query("FROM PageEntity p WHERE p.tenantId = :tenantId AND p.id = :id")
  Optional<PageEntity> getOneById(String tenantId, Long id);
}
