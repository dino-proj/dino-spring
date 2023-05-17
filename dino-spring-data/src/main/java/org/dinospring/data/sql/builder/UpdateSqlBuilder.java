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

package org.dinospring.data.sql.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 19:20:39
 */

public class UpdateSqlBuilder extends WhereSql<UpdateSqlBuilder> {

  protected List<String> updateColumns = new ArrayList<>();

  protected List<Object> updateParams = new ArrayList<>();

  /**
     * 根据表名构造, 如下写法都是合法的：
     * <p>- <code>"table1"</code>
     * <p>- <code>"table1 as t1"</code>
     *
     * @param table
     */
  public UpdateSqlBuilder(final String table) {
    setThat(this);
    this.table(table);
  }

  /**
     * 设置表名，更设置表的别名
     * <p>- <code>生成的sql片段为：table AS alias</code>
     *
     * @param table
     * @param alias
     */
  public UpdateSqlBuilder(final String table, final String alias) {
    setThat(this);
    this.table(table + " AS " + alias);
  }

  public UpdateSqlBuilder set(String expr, Object value) {
    if (StringUtils.contains(expr, '?')) {
      this.updateColumns.add(expr);
    } else {
      this.updateColumns.add(expr + "=?");
    }
    this.updateParams.add(value);
    return this;
  }

  public UpdateSqlBuilder setIf(final boolean cnd, String expr, Object value) {
    if (cnd) {
      return set(expr, value);
    }
    return this;
  }

  public UpdateSqlBuilder setIfNotNull(String expr, Object value) {
    return setIf(!Objects.isNull(value), expr, value);
  }

  public UpdateSqlBuilder set(String expr) {
    this.updateColumns.add(expr);
    return this;
  }

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
