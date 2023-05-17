// Copyright 2021 dinodev.cn
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.data.sql.builder.DeleteSqlBuilder;
import org.dinospring.data.sql.builder.InsertSqlBuilder;
import org.dinospring.data.sql.builder.SelectSqlBuilder;
import org.dinospring.data.sql.builder.UpdateSqlBuilder;
import org.dinospring.data.sql.dialect.Dialect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

/**
 *
 * @author tuuboo
 */

@NoRepositoryBean
public interface JdbcSelectExecutor<T, K> extends JdbcHelperExcutor<T, K> {

  /**
   * 数据库的Dialect
   * @return
   */
  Dialect dialect();

  /**
   * 针对此Entity的新的查询，并自动添加TenantId
   * @return
   */
  default SelectSqlBuilder newSelect() {
    var select = new SelectSqlBuilder(dialect(), this.tableName());
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      select.eq("tenant_id", ContextHelper.currentTenantId());
    }
    return select;
  }

  /**
   * 针对此Entity的新的查询，不添加TenantId
   * @return
   */
  default SelectSqlBuilder newSelectWithoutTenant() {
    return new SelectSqlBuilder(dialect(), this.tableName());
  }

  /**
   * 针对此Entity的新的查询, 并自动添加TenantId
   * @param tableAlias 表的别名
   * @return
   */
  default SelectSqlBuilder newSelect(String tableAlias) {
    Assert.hasText(tableAlias, "tableAlias is empty");
    var select = new SelectSqlBuilder(dialect(), this.tableName(), tableAlias);
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      select.eq(String.format("%s.%s", tableAlias, "tenant_id"), ContextHelper.currentTenantId());
    }
    return select;
  }

  /**
   * 针对此Entity的新的查询, 不添加TenantId
   * @param tableAlias 表的别名
   * @return
   */
  default SelectSqlBuilder newSelectWithoutTenant(String tableAlias) {
    Assert.hasText(tableAlias, "tableAlias is empty");
    return new SelectSqlBuilder(dialect(), this.tableName(), tableAlias);
  }

  /**
   * 生成指定Entity的查询
   * @param entity entity class
   * @param tableAlias 表的别名
   * @return
   */
  default <E> SelectSqlBuilder newSelect(Class<E> entity, String tableAlias) {
    return StringUtils.isEmpty(tableAlias) ? new SelectSqlBuilder(dialect(), this.tableName(entity))
        : new SelectSqlBuilder(dialect(), this.tableName(entity), tableAlias);
  }

  /**
   * 针对Entity的删除
   * @return
   */
  default DeleteSqlBuilder newDelete() {
    var delete = new DeleteSqlBuilder(this.tableName());
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      delete.eq("tenant_id", ContextHelper.currentTenantId());
    }
    return delete;
  }

  /**
   * 针对Entity的删除
   * @param tableAlias
   * @return
   */
  default DeleteSqlBuilder newDelete(String tableAlias) {
    Assert.hasText(tableAlias, "tableAlias is empty");
    var delete = new DeleteSqlBuilder(this.tableName(), tableAlias);
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      delete.eq(String.format("%s.%s", tableAlias, "tenant_id"), ContextHelper.currentTenantId());
    }
    return delete;
  }

  /**
   * 针对此Entity的新的修改
   * @return
   */
  default UpdateSqlBuilder newUpdate() {
    var update = new UpdateSqlBuilder(this.tableName());
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      update.eq("tenant_id", ContextHelper.currentTenantId());
    }
    return update;
  }

  /**
   * 针对此Entity的新的修改
   * @param alias
   * @return
   */
  default UpdateSqlBuilder newUpdate(String alias) {
    var update = new UpdateSqlBuilder(this.tableName(), alias);
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      update.eq(String.format("%s.%s", alias, "tenant_id"), ContextHelper.currentTenantId());
    }
    return update;
  }

  /**
   * 针对此Entity的新的新增
   * @return
   */
  default InsertSqlBuilder newInsert() {
    var insert = new InsertSqlBuilder(this.tableName());
    if (entityMeta().isTenantRow() && Objects.nonNull(ContextHelper.currentTenantId())) {
      insert.set("tenant_id", ContextHelper.currentTenantId());
    }
    return insert;
  }

  /**
   * 查出所有主键记录
   * @param ids 主键集合
   * @return
   */
  default List<T> findAllById(Collection<K> ids) {
    var sql = newSelect();
    sql.in("id", ids);
    return this.queryList(sql);
  }

  /**
  * 查出所有主键记录
  * @param <C>
  * @param ids 主键集合
  * @param cls 类型
  * @return
  */
  default <C> List<C> findAllById(Collection<K> ids, Class<C> cls) {
    var sql = newSelect();
    sql.in("id", ids);
    return this.queryList(sql, cls);
  }

  /**
   * 查出所有记录
   * @param <C>
   * @param cls 类型
   * @return
   */
  default <C> List<C> findAll(Class<C> cls) {
    var sql = newSelect();
    return this.queryList(sql, cls);
  }

  /**
   * Query list
   * @param sql
   * @return
   */
  default List<T> queryList(SelectSqlBuilder sql) {
    return queryList(sql, entityClass());
  }

  /**
   * Query list
   * @param sql
   * @param sort 排序
   * @return
   */
  default List<T> queryList(SelectSqlBuilder sql, Sort sort) {
    return queryList(sql, entityClass(), sort);
  }

  /**
   * Query list
   * @param <P>
   * @param sql
   * @param clazz 结果类
   * @return
   */
  default <P> List<P> queryList(SelectSqlBuilder sql, Class<P> clazz) {
    return queryList(sql.getSql(), clazz, sql.getParams());
  }

  /**
   * Query list
   * @param <P>
   * @param sql
   * @param clazz 结果类
   * @param sort 排序
   * @return
   */
  default <P> List<P> queryList(SelectSqlBuilder sql, Class<P> clazz, Sort sort) {
    if (!Objects.isNull(sort) && sort.isSorted()) {
      sort.forEach(o -> sql.orderBy(o.getProperty(), o.isAscending()));
    }
    return queryList(sql.getSql(), clazz, sql.getParams());
  }

  /**
   * 根据指定的Native sql。查询列表
   * @param <P> Projection类型
   * @param sql Native sql 语句
   * @param clazz 要映射的类
   * @param params 查询参数
   * @return 如果结果为空，则返回 emptyList
   */
  <P> List<P> queryList(@Nonnull String sql, @Nonnull Class<P> clazz, @Nullable Object... params);

  /**
   * 获取一个元素
   * @param sql sql语句
   * @return null如果查不到
   */
  default T getOne(SelectSqlBuilder sql) {
    List<T> rs = queryList(sql);
    if (CollectionUtils.isNotEmpty(rs)) {
      return rs.get(0);
    } else {
      return null;
    }
  }

  /**
   * 获取一个元素
   * @param <P>
   * @param sql sql语句
   * @param clazz 结果类
   * @return null如果查不到
   */
  default <P> P getOne(SelectSqlBuilder sql, Class<P> clazz) {
    List<P> rs = queryList(sql, clazz);
    if (CollectionUtils.isNotEmpty(rs)) {
      return rs.get(0);
    } else {
      return null;
    }
  }

  /**
   * 获取一个元素
   * @param <P>
   * @param sql native sql语句
   * @param clazz 结果类
   * @param params 查询参数
   * @return null如果查不到
   */
  default <P> P getOne(String sql, Class<P> clazz, Object... params) {
    List<P> rs = queryList(sql, clazz, params);
    if (CollectionUtils.isNotEmpty(rs)) {
      return rs.get(0);
    } else {
      return null;
    }
  }

  /**
   * 返回计数
   * @param sql 查询语句
   * @return
   */
  default long count(SelectSqlBuilder sql) {
    Long l = getOne(sql, Long.class);
    return l == null ? 0L : l.longValue();
  }

  /**
   * 返回计数
   * @param sql native sql语句
   * @param params 查询参数
   * @return
   */
  default long count(String sql, Object... params) {
    Long l = getOne(sql, Long.class, params);
    return l == null ? 0L : l.longValue();
  }

  /**
   * 将查询结果放到Map中
   * @param <MK> key的类型
   * @param <MV> value的类型
   * @param sql
   * @param keyColumn 作key的列名
   * @param keyClass key的Class
   * @param valueClass value的Class
   * @return
   */
  default <MK, MV> Map<MK, MV> queryForMap(SelectSqlBuilder sql, String keyColumn, Class<MK> keyClass,
      Class<MV> valueClass) {
    return queryForMap(sql.getSql(), keyColumn, keyClass, valueClass, sql.getParams());
  }

  /**
   * 将查询结果放到Map中
   * @param <MK> key的类型
   * @param <MV> value的类型
   * @param sql
   * @param keyColumn 作key的列名
   * @param keyClass key的Class
   * @param valueColumn 作value的列名
   * @param valueClass value的Class
   * @return
   */
  <MK, MV> Map<MK, MV> queryForMap(SelectSqlBuilder sql, String keyColumn, Class<MK> keyClass, String valueColumn,
      Class<MV> valueClass);

  /**
   * 将查询结果放到Map中
   * @param <MK> key的类型
   * @param <MV> value的类型
   * @param sql
   * @param keyColumn 作key的列名
   * @param keyClass key的Class
   * @param valueClass value的Class
   * @param params 查询参数
   * @return
   */
  <MK, MV> Map<MK, MV> queryForMap(String sql, String keyColumn, Class<MK> keyClass, Class<MV> valueClass,
      Object... params);

  /**
   * 分页查询
   * @param sql sql查询
   * @param pageable 分页信息
   * @return
   */
  default Page<T> queryPage(SelectSqlBuilder sql, Pageable pageable) {
    return queryPage(sql, pageable, entityClass());
  }

  /**
   * 分页查询
   * @param <P>
   * @param sql sql查询
   * @param pageable 分页信息
   * @param clazz 结果类
   * @return
   */
  default <P> Page<P> queryPage(SelectSqlBuilder sql, Pageable pageable, Class<P> clazz) {

    Sort sort = pageable.getSort();
    if (sort.isSorted()) {
      sort.forEach(o -> sql.orderBy(o.getProperty(), o.isAscending()));
    }
    if (pageable.isUnpaged()) {
      return new PageImpl<>(queryList(sql, clazz));
    } else {
      sql.limit(pageable.getPageSize(), pageable.getOffset());
      return new PageImpl<>(queryList(sql, clazz), pageable, count(sql.getCountSql(), sql.getParams()));
    }
  }

  /**
   * 分页查询
   * @param sql sql查询
   * @param countSql 总记录数sql查询
   * @param pageable 分页信息
   * @return
   */
  default Page<T> queryPage(SelectSqlBuilder sql, SelectSqlBuilder countSql, Pageable pageable) {
    return queryPage(sql, countSql, pageable, entityClass());
  }

  /**
   * 分页查询
   * @param <P>
   * @param sqlsql查询
   * @param countSql 总记录数sql查询
   * @param pageable 分页信息
   * @param clazz 结果类
   * @return
   */
  default <P> Page<P> queryPage(SelectSqlBuilder sql, SelectSqlBuilder countSql, Pageable pageable, Class<P> clazz) {

    Sort sort = pageable.getSort();
    if (sort.isSorted()) {
      sort.forEach(o -> sql.orderBy(o.getProperty(), o.isAscending()));
    }
    if (pageable.isUnpaged()) {
      return new PageImpl<>(queryList(sql, clazz));
    } else {
      sql.limit(pageable.getPageSize(), pageable.getOffset());
      return new PageImpl<>(queryList(sql, clazz), pageable, count(countSql));
    }
  }

  /**
   * 保存并返回对象
   * @param sql native sql
   * @param params sql参数
   * @return
   */
  K save(@Nonnull String sql, @Nullable Object... params);
}
