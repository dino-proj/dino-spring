package org.dinospring.data.dao;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.botbrain.dino.sql.builder.SelectSqlBuilder;
import com.botbrain.dino.sql.dialect.Dialect;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JdbcSelectExecutor<T, K> extends JpaHelperExcutor<T, K> {

  Dialect getDialect();

  default SelectSqlBuilder newSelect() {
    return new SelectSqlBuilder(getDialect());
  }

  default SelectSqlBuilder newSelect(String tableAlias) {
    return StringUtils.isEmpty(tableAlias) ? new SelectSqlBuilder(getDialect(), this.tableName())
        : new SelectSqlBuilder(getDialect(), this.tableName(), tableAlias);
  }

  default <E> SelectSqlBuilder newSelect(Class<E> entity, String tableAlias) {
    return StringUtils.isEmpty(tableAlias) ? new SelectSqlBuilder(getDialect(), this.tableName(entity))
        : new SelectSqlBuilder(getDialect(), this.tableName(entity), tableAlias);
  }

  default List<T> queryList(SelectSqlBuilder sql) {
    return queryList(sql, getEntityClass());
  }

  /**
   * 根据指定的Native sql。查询列表
   * @param <P> Projection类型
   * @param sql Native sql 语句
   * @param clazz 要映射的类
   * @param params 查询参数
   * @return
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

  default <P> P getOne(String sql, Class<P> clazz, Object... params) {
    List<P> rs = queryList(sql, clazz, params);
    if (CollectionUtils.isNotEmpty(rs)) {
      return rs.get(0);
    } else {
      return null;
    }
  }

  default <P> List<P> queryList(SelectSqlBuilder sql, Class<P> clazz) {
    return queryList(sql.getSql(), clazz, sql.getParams());
  }

  default <P> P getOne(SelectSqlBuilder sql, Class<P> clazz) {
    List<P> rs = queryList(sql, clazz);
    if (CollectionUtils.isNotEmpty(rs)) {
      return rs.get(0);
    } else {
      return null;
    }
  }

  default long count(SelectSqlBuilder sql) {
    Long l = getOne(sql, Long.class);
    return l == null ? 0L : l.longValue();
  }

  default long count(String sql, Object... params) {
    Long l = getOne(sql, Long.class, params);
    return l == null ? 0L : l.longValue();
  }

  default Page<T> queryPage(SelectSqlBuilder sql, Pageable pageable) {
    return queryPage(sql, pageable, getEntityClass());
  }

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

  default Page<T> queryPage(SelectSqlBuilder sql, SelectSqlBuilder countSql, Pageable pageable) {
    return queryPage(sql, countSql, pageable, getEntityClass());
  }

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

  K save(@Nonnull String sql, @Nullable Object... params);
}
