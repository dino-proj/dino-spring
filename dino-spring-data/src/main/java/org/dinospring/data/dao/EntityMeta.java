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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.utils.NamingUtils;
import org.dinospring.data.domain.EntityBase;
import org.dinospring.data.domain.LogicalDelete;
import org.dinospring.data.domain.TenantLevel;
import org.dinospring.data.domain.TenantRowEntity;
import org.dinospring.data.domain.TenantTableEntity;
import org.dinospring.data.domain.Versioned;
import org.dinospring.data.sql.dialect.Dialect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.util.Lazy;
import org.springframework.util.Assert;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author tuuboo
 */

@Slf4j
@Data
public class EntityMeta {

  private static final Map<Class<?>, EntityMeta> ENTITY_META_CACHE = new HashMap<>();

  private static final Lazy<Dialect> DEFAULT_DIALECT = Lazy.of(() -> ContextHelper.findBean(Dialect.class));

  private final Dialect dialect;

  private final Class<?> domainClass;

  private final TenantLevel tenantLevel;

  private final String tableName;

  private final boolean logicalDelete;

  private final boolean versioned;

  private final Lazy<String> quotedTableName;

  private EntityMeta(Dialect dialect, Class<?> domainClass, TenantLevel tenantLevel, String tableName,
                     boolean logicalDelete, boolean versioned) {
    this.dialect = dialect;
    this.domainClass = domainClass;
    this.tenantLevel = tenantLevel;
    this.tableName = tableName;
    this.logicalDelete = logicalDelete;
    this.versioned = versioned;
    this.quotedTableName = Lazy.of(() -> dialect.quoteTableName(tableName));
    ENTITY_META_CACHE.put(domainClass, this);
  }

  /**
   * 是否是Tenant Table表
   * @return
   */
  public boolean isTenantTable() {
    return tenantLevel == TenantLevel.TABLE;
  }

  /**
   * 是否是Tenant Row表
   * @return
   */
  public boolean isTenantRow() {
    return tenantLevel == TenantLevel.ROW;
  }

  /**
   * 是否是Tenant Schema表
   * @return
   */
  public boolean isTenantSchema() {
    return tenantLevel == TenantLevel.SCHEMA;
  }

  /**
   * 是否是Tenant表
   * @return
   */
  public boolean isTenantable() {
    return isTenantTable() || isTenantRow() || isTenantSchema();
  }

  /**
   * 返回Quoted表名，如果是Tenant表，则在表名后面加上TenantId
   * @return
   */
  public String getQuotedTableName() {
    if (isTenantTable()) {
      return dialect.quoteTableName(tableName + '_' + ContextHelper.currentTenantId());
    } else {
      return quotedTableName.get();
    }
  }

  public static EntityMeta of(Dialect dialect, Class<?> cls) {
    var cachedMeta = ENTITY_META_CACHE.get(cls);
    if(Objects.nonNull(cachedMeta)){
      return cachedMeta;
    }

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

    var versioned = Versioned.class.isAssignableFrom(cls);

    return new EntityMeta(dialect, cls, tenantLevel, tableName, logicalDelete, versioned);
  }

  public static EntityMeta of(Class<?> cls) {
    return of(DEFAULT_DIALECT.get(), cls);
  }
}
