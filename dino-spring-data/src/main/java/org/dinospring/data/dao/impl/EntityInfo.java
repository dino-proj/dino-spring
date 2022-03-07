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

package org.dinospring.data.dao.impl;

import javax.persistence.Table;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.utils.NamingUtils;
import org.dinospring.data.domain.EntityBase;
import org.dinospring.data.domain.LogicalDelete;
import org.dinospring.data.domain.TenantLevel;
import org.dinospring.data.domain.TenantRowEntity;
import org.dinospring.data.domain.TenantTableEntity;
import org.dinospring.data.sql.dialect.Dialect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
@Data
public class EntityInfo {
  private final Dialect dialect;

  private final Class<?> domainClass;

  private final TenantLevel tenantLevel;

  private final String tableName;

  private final boolean logicalDelete;

  private final String quotedTableName;

  private EntityInfo(Dialect dialect, Class<?> domainClass, TenantLevel tenantLevel, String tableName,
      boolean logicalDelete) {
    this.dialect = dialect;
    this.domainClass = domainClass;
    this.tenantLevel = tenantLevel;
    this.tableName = tableName;
    this.logicalDelete = logicalDelete;

    if (!isTenantTable()) {
      this.quotedTableName = dialect.quoteTableName(tableName);
    } else {
      this.quotedTableName = null;
    }
  }

  /**
   * 是否是Tenant表
   * @return
   */
  public boolean isTenantTable() {
    return tenantLevel == TenantLevel.TABLE;
  }

  /**
   * 返回Quoted表名，如果是Tenant表，则在表名后面加上TenantId
   * @return
   */
  public String getQuotedTableName() {
    if (isTenantTable()) {
      return dialect.quoteTableName(tableName + '_' + ContextHelper.currentTenantId());
    } else {
      return quotedTableName;
    }
  }

  public static EntityInfo of(Dialect dialect, Class<?> cls) {
    var tenantLevel = TenantLevel.NOT;
    if (TenantRowEntity.class.isAssignableFrom(cls)) {
      tenantLevel = TenantLevel.ROW;
    }
    if (TenantTableEntity.class.isAssignableFrom(cls)) {
      tenantLevel = TenantLevel.TABLE;
    }

    String tableName;
    var tableAnno = AnnotationUtils.findAnnotation(cls, Table.class);
    if (tableAnno != null) {
      tableName = tableAnno.name();
    } else {
      tableName = NamingUtils.toSnake(cls.getSimpleName());
    }

    var logicalDelete = LogicalDelete.class.isAssignableFrom(cls);
    if (logicalDelete && !EntityBase.class.isAssignableFrom(cls)) {
      log.error("entity:{} is LogicalDelete but not extends EntityBase", cls.getName());
      Assert.isAssignable(EntityBase.class, cls, "");
    }

    return new EntityInfo(dialect, cls, tenantLevel, tableName, logicalDelete);
  }
}
