// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.sql.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL UPDATE语句构建器
 * 用于构建UPDATE SQL语句的Builder类，支持SET子句和WHERE条件等
 *
 * @author Cody Lu
 * @date 2022-03-07 19:20:39
 */

public final class UpdateSqlBuilder extends WhereSql<UpdateSqlBuilder> {

  private final List<String> updateColumns = new ArrayList<>();

  private final List<Object> updateParams = new ArrayList<>();

  /**
   * 私有构造函数，防止直接实例化
   * @param table 表名
   */
  private UpdateSqlBuilder(final String table) {
    this.tables.add(table);
  }

  /**
   * 私有构造函数，防止直接实例化
   * @param table 表名
   * @param alias 表别名
   */
  private UpdateSqlBuilder(final String table, final String alias) {
    this.tables.add(table + " AS " + alias);
  }

  /**
   * 根据表名创建UPDATE语句构建器
   * <p>支持的格式：
   * <p>- <code>"table1"</code>
   * <p>- <code>"table1 as t1"</code>
   *
   * @param table 表名
   * @return 配置好的UpdateSqlBuilder实例
   */
  public static UpdateSqlBuilder create(final String table) {
    UpdateSqlBuilder builder = new UpdateSqlBuilder(table);
    builder.initializeBuilder();
    return builder;
  }

  /**
   * 根据表名和别名创建UPDATE语句构建器
   * <p>生成的sql片段为：table AS alias
   *
   * @param table 表名
   * @param alias 表别名
   * @return 配置好的UpdateSqlBuilder实例
   */
  public static UpdateSqlBuilder create(final String table, final String alias) {
    UpdateSqlBuilder builder = new UpdateSqlBuilder(table, alias);
    builder.initializeBuilder();
    return builder;
  }

  /**
   * 初始化构建器
   */
  private void initializeBuilder() {
    setThat(this);
  }

  /**
   * 设置列的更新值
   * @param expr 列表达式，可以包含?占位符
   * @param value 参数值
   * @return 当前构建器实例
   */
  public UpdateSqlBuilder set(String expr, Object value) {
    if (StringUtils.contains(expr, '?')) {
      this.updateColumns.add(expr);
    } else {
      this.updateColumns.add(expr + "=?");
    }
    this.updateParams.add(value);
    return this;
  }

  /**
   * 根据条件设置列的更新值
   * @param cnd 条件，为true时才设置
   * @param expr 列表达式
   * @param value 参数值
   * @return 当前构建器实例
   */
  public UpdateSqlBuilder setIf(final boolean cnd, String expr, Object value) {
    if (cnd) {
      return set(expr, value);
    }
    return this;
  }

  /**
   * 当值不为null时设置列的更新值
   * @param expr 列表达式
   * @param value 参数值
   * @return 当前构建器实例
   */
  public UpdateSqlBuilder setIfNotNull(String expr, Object value) {
    return setIf(!Objects.isNull(value), expr, value);
  }

  /**
   * 设置列的更新表达式（不添加参数）
   * @param expr 列表达式
   * @return 当前构建器实例
   */
  public UpdateSqlBuilder set(String expr) {
    this.updateColumns.add(expr);
    return this;
  }

  /**
   * 根据条件设置列的更新表达式（不添加参数）
   * @param cnd 条件，为true时才设置
   * @param expr 列表达式
   * @return 当前构建器实例
   */
  public UpdateSqlBuilder setIf(final boolean cnd, String expr) {
    if (cnd) {
      return set(expr);
    }
    return this;
  }

  @Override
  public String getSql() {
    final StringBuilder sql = new StringBuilder(64);
    if (withSql != null) {
      sql.append("WITH ").append(withName).append(" AS (\n").append(withSql.getSql()).append("\n)\n");
    }
    sql.append("UPDATE ");
    appendList(sql, tables, " ", ", ");
    appendList(sql, updateColumns, " SET ", ", ");
    appendList(sql, whereColumns, " WHERE ", " ");
    return sql.toString();
  }

  @Override
  public Object[] getParams() {

    Stream<Object[]> paramsArr = Stream.of(withSql == null ? EMPTY_PARAMS : withSql.getParams(), updateParams.toArray(),
        whereParams.toArray());

    return paramsArr.flatMap(Arrays::stream).toArray();
  }

  @Override
  public String toString() {
    return this.getSql();
  }

}
