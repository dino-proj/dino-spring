// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.utils.NamingUtils;
import cn.dinodev.spring.data.domain.EntityBase;
import cn.dinodev.spring.data.domain.LogicalDelete;
import cn.dinodev.spring.data.domain.TenantLevel;
import cn.dinodev.spring.data.domain.TenantRowEntity;
import cn.dinodev.spring.data.domain.TenantTableEntity;
import cn.dinodev.spring.data.domain.Versioned;
import cn.dinodev.spring.data.sql.dialect.Dialect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.util.Lazy;
import org.springframework.util.Assert;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
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
    if (Objects.nonNull(cachedMeta)) {
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
