package org.dinospring.data.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.collections4.IteratorUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.data.dao.EntityMeta;
import org.dinospring.data.dao.JdbcSelectExecutor;
import org.dinospring.data.sql.builder.DeleteSqlBuilder;
import org.dinospring.data.sql.builder.SelectSqlBuilder;
import org.dinospring.data.sql.builder.UpdateSqlBuilder;
import org.dinospring.data.sql.dialect.Dialect;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 18:54:45
 */

public class DinoJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID>, JdbcSelectExecutor<T, ID> {
  protected static final Map<Class<?>, EntityMeta> ENTITY_INFO_CACHE = new HashMap<>();

  protected final Dialect dialect;
  protected final EntityMeta entityInfo;

  private final JpaEntityInformation<T, ?> entityInformation;
  private final EntityManager em;
  private final PersistenceProvider provider;

  private final JdbcTemplate jdbcTemplate;

  private @Nullable CrudMethodMetadata metadata;
  private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;

  public DinoJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {

    Assert.notNull(entityInformation, "JpaEntityInformation must not be null!");
    Assert.notNull(entityManager, "EntityManager must not be null!");

    this.dialect = ContextHelper.findBean(Dialect.class);

    this.entityInformation = entityInformation;
    this.em = entityManager;
    this.provider = PersistenceProvider.fromEntityManager(entityManager);

    this.jdbcTemplate = ContextHelper.findBean(JdbcTemplate.class);

    this.entityInfo = EntityMeta.of(this.dialect, entityInformation.getJavaType());
    ENTITY_INFO_CACHE.put(entityInformation.getJavaType(), entityInfo);
  }

  public DinoJpaRepository(Class<T> domainClass, EntityManager em) {
    this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
  }

  @Override
  public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
    this.metadata = crudMethodMetadata;

  }

  @Override
  public void setEscapeCharacter(EscapeCharacter escapeCharacter) {
    this.escapeCharacter = escapeCharacter;
  }

  @Nullable
  protected CrudMethodMetadata getRepositoryMethodMetadata() {
    return metadata;
  }

  protected Class<T> getDomainClass() {
    return entityInformation.getJavaType();
  }

  @Override
  public EntityMeta entityMeta() {
    return this.entityInfo;
  }

  @Override
  public List<T> findAll() {
    var sql = newSelect();
    return queryList(sql);
  }

  @Override
  public List<T> findAll(Sort sort) {
    var sql = newSelect();
    return queryList(sql, sort);
  }

  @Override
  public List<T> findAllById(Iterable<ID> ids) {
    if (Objects.isNull(ids)) {
      return Collections.emptyList();
    }
    var sql = newSelect();
    sql.in("id", IteratorUtils.toList(ids.iterator()));
    return queryList(sql);
  }

  @Override
  public <S extends T> List<S> saveAll(Iterable<S> entities) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends T> S saveAndFlush(S entity) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<T> entities) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<ID> ids) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAllInBatch() {
    // TODO Auto-generated method stub

  }

  @Override
  public T getOne(ID id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public T getById(ID id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends T> S save(S entity) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<T> findById(ID id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean existsById(ID id) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public long count() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void deleteById(ID id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(T entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAllById(Iterable<? extends ID> ids) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAll(Iterable<? extends T> entities) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAll() {
    // TODO Auto-generated method stub

  }

  @Override
  public <S extends T> Optional<S> findOne(Example<S> example) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends T> long count(Example<S> example) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public <S extends T> boolean exists(Example<S> example) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Optional<T> findOne(Specification<T> spec) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<T> findAll(Specification<T> spec) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<T> findAll(Specification<T> spec, Sort sort) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long count(Specification<T> spec) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public EntityManager entityManager() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<T> entityClass() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<ID> keyClass() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <C> String tableName(Class<C> entityClass) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toJson(Object obj) throws JsonProcessingException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <C> C fromJson(String json, Class<C> cls) throws JsonProcessingException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Dialect dialect() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <P> List<P> queryList(String sql, Class<P> clazz, Object... params) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <MK, MV> Map<MK, MV> queryForMap(SelectSqlBuilder sql, String keyColumn, Class<MK> keyClass,
      String valueColumn, Class<MV> valueClass) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <MK, MV> Map<MK, MV> queryForMap(String sql, String keyColumn, Class<MK> keyClass, Class<MV> valueClass,
      Object... params) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ID save(String sql, Object... params) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean updateById(ID id, Map<String, Object> columnValue) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean updateByIdWithVersion(ID id, Map<String, Object> columnValue, Number version) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public <S extends T, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long update(UpdateSqlBuilder updateSqlBuilder) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long delete(DeleteSqlBuilder deleteSqlBuilder) {
    // TODO Auto-generated method stub
    return 0;
  }

}
