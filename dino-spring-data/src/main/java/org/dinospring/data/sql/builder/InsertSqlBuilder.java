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

package org.dinospring.data.sql.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dinospring.data.sql.SqlBuilder;

/**
 *
 * @author tuuboo
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
