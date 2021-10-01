// Copyright 2021 dinospring.cn
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
