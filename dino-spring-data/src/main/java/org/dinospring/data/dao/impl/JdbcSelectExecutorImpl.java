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

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;

import com.botbrain.dino.sql.dialect.Dialect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.data.dao.JdbcSelectExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.ClassUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcSelectExecutorImpl<T, K> extends SimpleJpaRepository<T, K> implements JdbcSelectExecutor<T, K> {

  private static final Map<Class<?>, EntityInfo> ENTITY_INFO_CACHE = new HashMap<>();

  private final JpaEntityInformation<T, K> entityInformation;
  private final EntityManager entityManager;

  private JdbcTemplate jdbcTemplate;

  private Dialect dialect;

  private ObjectMapper objectMapper;

  public JdbcSelectExecutorImpl(JpaEntityInformation<T, K> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityInformation = entityInformation;
    this.entityManager = entityManager;
    init();
  }

  public JdbcSelectExecutorImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityInformation = getEntityInformation(domainClass, entityManager);
    this.entityManager = entityManager;
    init();
  }

  private void init() {
    this.jdbcTemplate = ContextHelper.findBean(JdbcTemplate.class);
    this.objectMapper = ContextHelper.findBean(ObjectMapper.class);
    this.dialect = ContextHelper.findBean(Dialect.class);

    ENTITY_INFO_CACHE.put(getDomainClass(), EntityInfo.of(dialect, getEntityClass()));
  }

  @Override
  public EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  public Class<T> getEntityClass() {
    return getDomainClass();
  }

  @Override
  @SuppressWarnings("unchecked")
  @Nullable
  public Class<K> getKeyClass() {
    var tp = this.getDomainClass().getGenericSuperclass();
    if (tp instanceof ParameterizedType) {
      var paramType = (ParameterizedType) tp;
      return ((Class<K>) paramType.getActualTypeArguments()[0]);
    }
    return null;
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public <C> String tableName(Class<C> cls) {
    ENTITY_INFO_CACHE.computeIfAbsent(cls, c -> EntityInfo.of(dialect, c));
    var entityInfo = ENTITY_INFO_CACHE.get(cls);
    if (entityInfo.isTenantTable()) {
      Assert.notNull(ContextHelper.currentTenantId(), Status.CODE.FAIL_TENANT_NOT_EXIST);
      return dialect.quoteTableName(
          StringUtils.appendIfMissing(entityInfo.getTableName(), "_", "_") + ContextHelper.currentTenantId());
    }
    return entityInfo.getQuotedTableName();
  }

  @Override
  public <P> List<P> queryList(@Nonnull String sql, @Nonnull Class<P> clazz, @Nullable Object... params) {
    if (log.isDebugEnabled()) {
      log.debug("query for: {},\nSQL:{},\nPARAMs:", clazz, sql, params);
    }
    if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
      return jdbcTemplate.queryForList(sql, clazz, params);
    } else {
      return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(clazz), params);
    }
  }

  @Override
  public boolean updateColumnById(K id, Map<String, Object> columnValue) {
    return false;
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
    var k = this.getKeyClass();
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

  private JpaEntityInformation<T, K> getEntityInformation(Class<T> domainClass, EntityManager em) {
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

}
