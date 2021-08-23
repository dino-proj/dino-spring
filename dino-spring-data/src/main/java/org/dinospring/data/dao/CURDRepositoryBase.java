package org.dinospring.data.dao;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.data.domain.TenantableEntityBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CURDRepositoryBase<T, K> extends JpaRepository<T, K>, JdbcSelectExecutor<T, K> {
  /**
   * 对查询结果进行处理，自动注入TenantId
   * @param entity
   * @return
   */
  default T postQuery(T entity) {
    if (!Objects.isNull(entity) && entity instanceof TenantableEntityBase) {
      TenantableEntityBase<?> tenantEntity = (TenantableEntityBase<?>) entity;
      tenantEntity.setTenantId(ContextHelper.currentTenantId());
    }
    return entity;
  }

  /**
   * 对查询结果进行处理，自动注入TenantId
   * @param entity
   * @return
   */
  default List<T> postQuery(List<T> entities) {
    if (CollectionUtils.isNotEmpty(entities)) {
      entities.forEach(CURDRepositoryBase.this::postQuery);
    }
    return entities;
  }
}
