// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.dao.impl;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.data.dao.EntityMeta;
import org.dinospring.data.dao.JdbcSelectExecutor;
import org.dinospring.data.domain.Versioned;
import org.dinospring.data.sql.builder.DeleteSqlBuilder;
import org.dinospring.data.sql.builder.SelectSqlBuilder;
import org.dinospring.data.sql.builder.UpdateSqlBuilder;
import org.dinospring.data.sql.dialect.Dialect;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.util.CastUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
public class DinoJdbcRepositoryBase<T, K> extends SimpleJdbcRepository<T, K> implements JdbcSelectExecutor<T, K> {
  private final RelationalPersistentEntity<T> entity;

  private final EntityMeta entityInfo;

  @Nonnull
  private JdbcTemplate jdbcTemplate;

  @Nonnull
  private Dialect dialect;

  @Nonnull
  private ConversionService conversionService;

  public DinoJdbcRepositoryBase(JdbcAggregateOperations entityOperations, RelationalPersistentEntity<T> entity,
      JdbcConverter converter) {
    super(entityOperations, entity, converter);
    this.entity = entity;

    this.jdbcTemplate = ContextHelper.findBean(JdbcTemplate.class);
    this.dialect = ContextHelper.findBean(Dialect.class);
    this.conversionService = ContextHelper.findBean("dataConversionService", ConversionService.class);
    this.entityInfo = EntityMeta.of(this.dialect, this.entityClass());

  }

  @Override
  public Class<T> entityClass() {
    return this.entity.getType();
  }

  @Override
  public EntityMeta entityMeta() {
    return this.entityInfo;
  }

  @Override
  @SuppressWarnings("unchecked")
  @Nullable
  public Class<K> keyClass() {
    var tp = this.entity.getIdProperty();
    if (Objects.nonNull(tp)) {
      return (Class<K>) tp.getType();
    }
    return null;
  }

  @Override
  public Dialect dialect() {
    return this.dialect;
  }

  @Override
  public <C> String tableName(Class<C> cls) {
    var meta = EntityMeta.of(this.dialect, cls);
    return meta.getQuotedTableName();
  }

  @Override
  public <P> List<P> queryList(@Nonnull String sql, @Nonnull Class<P> clazz, @Nullable Object... params) {
    if (log.isDebugEnabled()) {
      log.debug("query for: {},\nSQL:{},\nPARAMs:", clazz, sql, params);
    }
    if (TypeUtils.isPrimitiveOrString(clazz)) {
      return this.jdbcTemplate.queryForList(sql, clazz, params);
    } else {
      return this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz, this.conversionService), params);
    }
  }

  @Override
  public <MK, MV> Map<MK, MV> queryForMap(SelectSqlBuilder sql, String keyColumn, Class<MK> keyClass,
      String valueColumn, Class<MV> valueClass) {
    if (log.isDebugEnabled()) {
      log.debug("query for map: {}:{}, {}:{},\nSQL:{},\nPARAMs:", keyColumn, keyClass, valueColumn, valueClass,
          sql.getSql(), sql.getParams());
    }
    org.springframework.util.Assert.isTrue(TypeUtils.isPrimitiveOrString(keyClass), "key must be primitive class");
    var isPrimitiveForValueColumn = TypeUtils.isPrimitiveOrString(valueClass);

    Map<MK, MV> result = new HashMap<>(20);
    this.jdbcTemplate.query(sql.getSql(), (RowCallbackHandler) rs -> {

      MK key = CastUtils
          .cast(Objects.requireNonNull(JdbcUtils.getResultSetValue(rs, rs.findColumn(keyColumn), keyClass)));
      if (isPrimitiveForValueColumn) {
        MV value = CastUtils
            .cast(Objects.requireNonNull(JdbcUtils.getResultSetValue(rs, rs.findColumn(valueColumn), valueClass)));
        result.put(key, value);
      } else {
        Object resultSetValue = JdbcUtils.getResultSetValue(rs, rs.findColumn(valueColumn));
        MV convert = DinoJdbcRepositoryBase.this.conversionService.convert(resultSetValue, valueClass);
        result.put(key, convert);
      }
    }, sql.getParams());
    return result;
  }

  @Override
  public <MK, MV> Map<MK, MV> queryForMap(String sql, String keyColumn, Class<MK> keyClass, Class<MV> valueClass,
      Object... params) {
    if (log.isDebugEnabled()) {
      log.debug("query for map: {}:{}, valueClass:{},\nSQL:{},\nPARAMs:", keyColumn, keyClass, valueClass, sql, params);
    }
    org.springframework.util.Assert.isTrue(TypeUtils.isPrimitiveOrString(keyClass), "key must be primitive class");

    boolean isPrimitiveValue = TypeUtils.isPrimitiveOrString(valueClass);
    BeanPropertyRowMapper<MV> mapper = isPrimitiveValue ? null
        : BeanPropertyRowMapper.newInstance(valueClass, this.conversionService);

    return this.jdbcTemplate.query(sql, (ResultSetExtractor<Map<MK, MV>>) rs -> {
      Map<MK, MV> result = new HashMap<>(20);
      if (isPrimitiveValue) {
        org.springframework.util.Assert.isTrue(rs.getMetaData().getColumnCount() == 2,
            "resulset column count must be 2,as valueClass is primitive class");
      }

      var keyIndex = rs.findColumn(keyColumn);
      int rowNum = 0;
      while (rs.next()) {
        MK key = CastUtils.cast(JdbcUtils.getResultSetValue(rs, keyIndex, keyClass));
        if (isPrimitiveValue) {
          MV value = CastUtils.cast(JdbcUtils.getResultSetValue(rs, 3 - keyIndex, valueClass));
          result.put(key, value);
        } else {
          MV value = mapper.mapRow(rs, rowNum++);
          result.put(key, value);
        }
      }
      return result;
    }, params);

  }

  @Override
  public K save(String sql, Object... params) {
    GeneratedKeyHolder keys = new GeneratedKeyHolder();
    var argSetter = new ArgumentPreparedStatementSetter(params);
    this.jdbcTemplate.update((PreparedStatementCreator) con -> {
      var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      argSetter.setValues(ps);
      return ps;
    }, keys);

    var idAttr = this.entity.getIdProperty();
    var map = keys.getKeys();
    if (idAttr == null || map == null || !map.containsKey(idAttr.getColumnName().getReference())) {
      return null;
    }
    var v = map.get(idAttr.getName()).toString();
    var k = this.keyClass();
    log.info("entity key:{} of value {}", k, v);

    if (v == null || k == null) {
      return null;
    }
    if (k.isAssignableFrom(Long.class)) {
      return k.cast(Long.valueOf(v));
    } else if (k.isAssignableFrom(String.class)) {
      return k.cast(v);
    } else if (k.isAssignableFrom(Integer.class)) {
      return k.cast(Integer.valueOf(v));
    }

    return null;
  }

  @Override
  public boolean updateById(K id, Map<String, Object> columnValue) {

    var sql = new UpdateSqlBuilder(this.tableName());
    sql.eq("id", id);
    for (var kv : columnValue.entrySet()) {
      var colProp = this.entity.getPersistentProperty(kv.getKey());
      var colName = Objects.isNull(colProp) ? kv.getKey() : colProp.getColumnName().getReference();
      sql.set(colName, kv.getValue());
    }
    return this.jdbcTemplate.update(sql.getSql(), sql.getParams()) == 1;
  }

  @Override
  public boolean updateByIdWithVersion(K id, Map<String, Object> columnValue, Number version) {
    org.springframework.util.Assert.isTrue(this.entityInfo.isVersioned(),
        this.entityInfo.getDomainClass() + " must implements " + Versioned.class);
    var sql = new UpdateSqlBuilder(this.tableName());
    sql.eq("id", id);
    sql.eq("version", version);
    for (var kv : columnValue.entrySet()) {
      var colProp = this.entity.getPersistentProperty(kv.getKey());
      var colName = Objects.isNull(colProp) ? kv.getKey() : colProp.getColumnName().getReference();
      sql.set(colName, kv.getValue());
    }
    sql.set("version = version+1");
    return this.jdbcTemplate.update(sql.getSql(), sql.getParams()) == 1;
  }

  @Override
  public long update(UpdateSqlBuilder updateSqlBuilder) {
    return this.jdbcTemplate.update(updateSqlBuilder.getSql(), updateSqlBuilder.getParams());
  }

  @Override
  public long delete(DeleteSqlBuilder deleteSqlBuilder) {
    return this.jdbcTemplate.update(deleteSqlBuilder.getSql(), deleteSqlBuilder.getParams());
  }

}
