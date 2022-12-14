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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.util.CastUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author tuuboo
 */

@Slf4j
public class JdbcSelectExecutorImpl<T, K> extends SimpleJpaRepository<T, K> implements JdbcSelectExecutor<T, K> {
  private final JpaEntityInformation<T, K> entityInformation;
  private final EntityManager entityManager;

  private final EntityMeta entityInfo;

  @Nonnull
  private JdbcTemplate jdbcTemplate;

  @Nonnull
  private Dialect dialect;

  @Nonnull
  private ObjectMapper objectMapper;

  @Nonnull
  private ConversionService conversionService;

  public JdbcSelectExecutorImpl(JpaEntityInformation<T, K> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityInformation = entityInformation;
    this.entityManager = entityManager;

    this.jdbcTemplate = ContextHelper.findBean(JdbcTemplate.class);
    this.objectMapper = ContextHelper.findBean(ObjectMapper.class);
    this.dialect = ContextHelper.findBean(Dialect.class);
    this.conversionService = ContextHelper.findBean("dataConversionService", ConversionService.class);
    this.entityInfo = EntityMeta.of(dialect, entityClass());


  }

  public JdbcSelectExecutorImpl(Class<T> domainClass, EntityManager entityManager) {
    this(getEntityInformation(domainClass, entityManager), entityManager);
  }

  @Override
  public EntityManager entityManager() {
    return this.entityManager;
  }

  @Override
  public Class<T> entityClass() {
    return getDomainClass();
  }

  @Override
  public EntityMeta entityMeta() {
    return this.entityInfo;
  }

  @Override
  @SuppressWarnings("unchecked")
  @Nullable
  public Class<K> keyClass() {
    var tp = this.getDomainClass().getGenericSuperclass();
    if (tp instanceof ParameterizedType) {
      var paramType = (ParameterizedType) tp;
      return ((Class<K>) paramType.getActualTypeArguments()[0]);
    }
    return null;
  }

  @Override
  public Dialect dialect() {
    return dialect;
  }

  @Override
  public <C> String tableName(Class<C> cls) {
    var meta = EntityMeta.of(dialect, cls);
    return meta.getQuotedTableName();
  }

  @Override
  public <P> List<P> queryList(@Nonnull String sql, @Nonnull Class<P> clazz, @Nullable Object... params) {
    if (log.isDebugEnabled()) {
      log.debug("query for: {},\nSQL:{},\nPARAMs:", clazz, sql, params);
    }
    if (TypeUtils.isPrimitiveOrString(clazz)) {
      return jdbcTemplate.queryForList(sql, clazz, params);
    } else {
      return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz, conversionService), params);
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
    jdbcTemplate.query(sql.getSql(), new RowCallbackHandler() {

      @Override
      public void processRow(ResultSet rs) throws SQLException {

        MK key = CastUtils.cast(Objects.requireNonNull(JdbcUtils.getResultSetValue(rs, rs.findColumn(keyColumn), keyClass)));
        if (isPrimitiveForValueColumn) {
          MV value = CastUtils.cast(Objects.requireNonNull(JdbcUtils.getResultSetValue(rs, rs.findColumn(valueColumn), valueClass)));
          result.put(key, value);
        } else {
          Object resultSetValue = JdbcUtils.getResultSetValue(rs, rs.findColumn(valueColumn));
          MV convert = conversionService.convert(resultSetValue, valueClass);
          result.put(key, convert);
        }
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
      : BeanPropertyRowMapper.newInstance(valueClass, conversionService);

    return jdbcTemplate.query(sql, new ResultSetExtractor<Map<MK, MV>>() {

      @Override
      public Map<MK, MV> extractData(ResultSet rs) throws SQLException, DataAccessException {
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
      }

    }, params);

  }

  @Override
  public K save(String sql, Object... params) {
    GeneratedKeyHolder keys = new GeneratedKeyHolder();
    var argSetter = new ArgumentPreparedStatementSetter(params);
    jdbcTemplate.update(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        argSetter.setValues(ps);
        return ps;
      }

    }, keys);

    var idAttr = this.entityInformation.getIdAttribute();
    var map = keys.getKeys();
    if (idAttr == null || map == null || !map.containsKey(idAttr.getName())) {
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

  private static <T, K> JpaEntityInformation<T, K> getEntityInformation(Class<T> domainClass, EntityManager em) {
    return new JpaMetamodelEntityInformation<>(domainClass, em.getMetamodel());
  }

  @Override
  public String toJson(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

  @Override
  public <C> C fromJson(String json, Class<C> cls) throws JsonProcessingException {
    return objectMapper.readValue(json, cls);
  }

  @Override
  public boolean updateById(K id, Map<String, Object> columnValue) {

    var sql = new UpdateSqlBuilder(tableName());
    sql.eq("id", id);
    for (var kv : columnValue.entrySet()) {
      sql.set(kv.getKey(), kv.getValue());
    }
    return jdbcTemplate.update(sql.getSql(), sql.getParams()) == 1;
  }

  @Override
  public boolean updateByIdWithVersion(K id, Map<String, Object> columnValue, Number version) {
    org.springframework.util.Assert.isTrue(entityInfo.isVersioned(),
      entityInfo.getDomainClass() + " must implements " + Versioned.class);
    var sql = new UpdateSqlBuilder(tableName());
    sql.eq("id", id);
    sql.eq("version", version);
    for (var kv : columnValue.entrySet()) {
      sql.set(kv.getKey(), kv.getValue());
    }
    sql.set("version = version+1");
    return jdbcTemplate.update(sql.getSql(), sql.getParams()) == 1;
  }

  @Override
  public long update(UpdateSqlBuilder updateSqlBuilder) {
    return jdbcTemplate.update(updateSqlBuilder.getSql(), updateSqlBuilder.getParams());
  }

  @Override
  public long delete(DeleteSqlBuilder deleteSqlBuilder) {
    return jdbcTemplate.update(deleteSqlBuilder.getSql(), deleteSqlBuilder.getParams());
  }

}
