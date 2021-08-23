package org.dinospring.core.modules.framework;

import java.util.Optional;

import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.data.jpa.repository.Query;

public interface PageRepository extends CURDRepositoryBase<PageEntity, Long> {

  @Query("FROM PageEntity p WHERE p.tenantId = :tenantId AND templateName = :templateName")
  Optional<PageEntity> getOneByTemplateName(String tenantId, String templateName);

  @Query("FROM PageEntity p WHERE p.tenantId = :tenantId AND id = :id")
  Optional<PageEntity> getOneById(String tenantId, Long id);
}
