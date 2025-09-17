// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.sql.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.dinodev.spring.data.sql.SqlBuilder;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:21:21
 */

public class InsertSqlBuilder implements SqlBuilder {

  private String table;

  protected List<String> setColumns = new ArrayList<>();

  protected List<String> valueExps = new ArrayList<>();

  protected List<Object> setParams = new ArrayList<>();

  public InsertSqlBuilder(String table) {
    this.table = table;
  }

  public InsertSqlBuilder set(String col, Object value) {
    return this.set(col, "?", value);
  }

  public InsertSqlBuilder set(String col, String valueExpr, Object value) {
    this.setColumns.add(col);
    this.valueExps.add(valueExpr);
    this.setParams.add(value);
    return this;
  }

  public InsertSqlBuilder setIf(final boolean cnd, String col, Object value) {
    return this.setIf(cnd, col, "?", value);
  }

  public InsertSqlBuilder setIf(final boolean cnd, String col, String valueExpr, Object value) {
    if (cnd) {
      return set(col, valueExpr, value);
    }
    return this;
  }

  public InsertSqlBuilder setIfNotNull(String col, Object value) {
    return setIf(!Objects.isNull(value), col, value);
  }

  public InsertSqlBuilder withoutParam(String col, String valueExpr) {
    this.setColumns.add(col);
    this.valueExps.add(valueExpr);
    return this;
  }

  public InsertSqlBuilder withoutParamIf(final boolean cnd, String col, String expr) {
    if (cnd) {
      return withoutParam(col, expr);
    }
    return this;
  }

  public InsertSqlBuilder setJson(String col, Object value) {
    return this.set(col, "?::json", value);
  }

  public InsertSqlBuilder setJsonIf(final boolean cnd, String col, Object value) {
    if (cnd) {
      return set(col, "?::json", value);
    }
    return this;
  }

  public InsertSqlBuilder setJsonIfNotNull(String col, Object value) {
    return setJsonIf(!Objects.isNull(value), col, value);
  }

  public InsertSqlBuilder setJsonb(String col, Object value) {
    return this.set(col, "?::jsonb", value);
  }

  public InsertSqlBuilder setJsonbIf(final boolean cnd, String col, Object value) {
    if (cnd) {
      return set(col, "?::jsonb", value);
    }
    return this;
  }

  public InsertSqlBuilder setJsonbIfNotNull(String col, Object value) {
    return setJsonIf(!Objects.isNull(value), col, value);
  }

  @Override
  public String getSql() {
    final StringBuilder sql = new StringBuilder(64);
    sql.append("INSERT INTO ").append(table);
    appendList(sql, setColumns, " (", ", ", ") ");
    appendList(sql, valueExps, " VALUES(", ", ", ") ");
    return sql.toString();
  }

  @Override
  public Object[] getParams() {
    return setParams.toArray(new Object[setParams.size()]);
  }

}
