// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework;

import java.util.Optional;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody Lu
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
