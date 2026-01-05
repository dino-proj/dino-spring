// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.dao.impl;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.data.dao.EntityMeta;
import cn.dinodev.spring.data.dao.JdbcSelectExecutor;
import cn.dinodev.spring.data.domain.Versioned;
import cn.dinodev.sql.builder.SelectSqlBuilder;
import cn.dinodev.sql.builder.UpdateSqlBuilder;
import cn.dinodev.sql.dialect.Dialect;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
public class DinoJdbcRepositoryBase<T, K> extends SimpleJdbcRepository<T, K> implements JdbcSelectExecutor<T, K> {
  private final RelationalPersistentEntity<T> entity;

  private final EntityMeta entityInfo;

  @Nonnull
  private final JdbcTemplate jdbcTemplate;

  @Nonnull
  private final Dialect dialectService;

  @Nonnull
  private final ConversionService conversionService;

  /**
   * 构造函数，初始化JDBC仓库基础组件
   * @param entityOperations JDBC聚合操作
   * @param entity 关系持久化实体
   * @param converter JDBC转换器
   */
  public DinoJdbcRepositoryBase(JdbcAggregateOperations entityOperations, RelationalPersistentEntity<T> entity,
      JdbcConverter converter) {
    super(entityOperations, entity, converter);
    this.entity = entity;

    this.jdbcTemplate = ContextHelper.findBean(JdbcTemplate.class);
    this.dialectService = ContextHelper.findBean(Dialect.class);
    this.conversionService = ContextHelper.findBean("dataConversionService", ConversionService.class);
    this.entityInfo = EntityMeta.of(this.dialectService, entity.getType());

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
    return this.dialectService;
  }

  @Override
  public <C> String tableName(Class<C> entityClass) {
    var meta = EntityMeta.of(this.dialectService, entityClass);
    return meta.getQuotedTableName();
  }

  @Override
  public <P> List<P> queryList(@Nonnull String sql, @Nonnull Class<P> clazz, @Nullable Object... params) {
    if (log.isDebugEnabled()) {
      log.debug("query for: {},\nSQL:{},\nPARAMs:{}", clazz, sql, params);
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
      log.debug("query for map: {}:{}, {}:{},\nSQL:{},\nPARAMs:{}", keyColumn, keyClass, valueColumn, valueClass,
          sql.getSql(), sql.getParams());
    }
    org.springframework.util.Assert.isTrue(TypeUtils.isPrimitiveOrString(keyClass), "key must be primitive class");
    var isPrimitiveForValueColumn = TypeUtils.isPrimitiveOrString(valueClass);

    Map<MK, MV> result = new HashMap<>(20);
    this.jdbcTemplate.query(sql.getSql(), (RowCallbackHandler) rs -> {

      MK key = TypeUtils
          .cast(Objects.requireNonNull(JdbcUtils.getResultSetValue(rs, rs.findColumn(keyColumn), keyClass)));
      if (isPrimitiveForValueColumn) {
        MV value = TypeUtils
            .cast(Objects.requireNonNull(JdbcUtils.getResultSetValue(rs, rs.findColumn(valueColumn), valueClass)));
        result.put(key, value);
      } else {
        Object resultSetValue = JdbcUtils.getResultSetValue(rs, rs.findColumn(valueColumn));
        MV convert = conversionService.convert(resultSetValue, valueClass);
        result.put(key, convert);
      }
    }, sql.getParams());
    return result;
  }

  @Override
  public <MK, MV> Map<MK, MV> queryForMap(String sql, String keyColumn, Class<MK> keyClass, Class<MV> valueClass,
      Object... params) {
    if (log.isDebugEnabled()) {
      log.debug("query for map: {}:{}, valueClass:{},\nSQL:{},\nPARAMs:{}", keyColumn, keyClass, valueClass, sql,
          params);
    }
    org.springframework.util.Assert.isTrue(TypeUtils.isPrimitiveOrString(keyClass), "key must be primitive class");

    if (TypeUtils.isPrimitiveOrString(valueClass)) {
      return this.jdbcTemplate.query(sql, (ResultSetExtractor<Map<MK, MV>>) rs -> {
        org.springframework.util.Assert.isTrue(rs.getMetaData().getColumnCount() == 2,
            "resulset column count must be 2,as valueClass is primitive class");

        var keyIndex = rs.findColumn(keyColumn);

        Map<MK, MV> result = new HashMap<>(20);
        while (rs.next()) {
          MK key = TypeUtils.cast(JdbcUtils.getResultSetValue(rs, keyIndex, keyClass));
          MV value = TypeUtils.cast(JdbcUtils.getResultSetValue(rs, 3 - keyIndex, valueClass));
          result.put(key, value);
        }
        return result;
      }, params);
    } else {
      BeanPropertyRowMapper<MV> mapper = BeanPropertyRowMapper.newInstance(valueClass, this.conversionService);

      return this.jdbcTemplate.query(sql, (ResultSetExtractor<Map<MK, MV>>) rs -> {
        Map<MK, MV> result = new HashMap<>(20);

        var keyIndex = rs.findColumn(keyColumn);
        int rowNum = 0;
        while (rs.next()) {
          MK key = TypeUtils.cast(JdbcUtils.getResultSetValue(rs, keyIndex, keyClass));
          MV value = mapper.mapRow(rs, rowNum++);
          result.put(key, value);
        }
        return result;
      }, params);
    }

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
    var value = map.get(idAttr.getName()).toString();
    var keyClass = this.keyClass();
    log.info("entity key:{} of value {}", keyClass, value);

    if (value == null || keyClass == null) {
      return null;
    }
    if (keyClass.isAssignableFrom(Long.class)) {
      return keyClass.cast(Long.valueOf(value));
    } else if (keyClass.isAssignableFrom(String.class)) {
      return keyClass.cast(value);
    } else if (keyClass.isAssignableFrom(Integer.class)) {
      return keyClass.cast(Integer.valueOf(value));
    }

    return null;
  }

  @Override
  public boolean updateById(K id, Map<String, Object> columnValue) {

    var sql = UpdateSqlBuilder.create(this.dialect(), this.tableName());
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
    var sql = UpdateSqlBuilder.create(this.dialect(), this.tableName());
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
  public int update(String sql, @Nullable Object... args) {
    return this.jdbcTemplate.update(sql, args);
  }

}
