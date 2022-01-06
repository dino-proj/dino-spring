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

package com.botbrain.dino.sql.builder;

import com.botbrain.dino.sql.dialect.Dialect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SelectSqlBuilder extends WhereSql<SelectSqlBuilder> {

  protected boolean distinct;

  protected List<String> columns = new ArrayList<>();

  protected List<JoinEntity<String>> joins = new ArrayList<>();

  protected List<String> groupBys = new ArrayList<>();

  protected List<String> havings = new ArrayList<>();

  protected List<JoinEntity<SelectSqlBuilder>> unions = new ArrayList<>();

  protected List<String> orderBys = new ArrayList<>();

  protected List<Object> havingParams = new ArrayList<>();

  private int limit = 0;

  private long offset = 0;

  private final Dialect dialect;

  public SelectSqlBuilder(final Dialect dialect) {
    this.dialect = dialect;
    setThat(this);
  }

  /**
   * 根据表名构造，表名，如下写法都是合法的：
   * <p>- <code>"table1"</code>
   * <p>- <code>"table1, table2"</code>
   * <p>- <code>"table1 as t1"</code>
   * <p>- <code>"table1 as t1 join table2 as t2 on t1.id=t2.id"</code>
   *
   * @param table
   */
  public SelectSqlBuilder(final Dialect dialect, final String table) {
    this(dialect);
    this.table(table);
  }

  /**
   * 设置表名，更设置表的别名
   * <p>- <code>生成的sql片段为：table AS alias</code>
   *
   * @param table
   * @param alias
   */
  public SelectSqlBuilder(final Dialect dialect, final String table, final String alias) {
    this(dialect);
    this.table(table + " AS " + alias);
  }

  /**
   * 带子查询的SqlBuilder
   * @param subQuery 子查询
   * @param alias 子查询的别名
   */
  public SelectSqlBuilder(final SelectSqlBuilder subQuery, final String alias) {
    this(subQuery.dialect);
    this.table(String.format("( %s ) AS %s", subQuery.getSql(), alias));
    this.whereParams.addAll(Arrays.asList(subQuery.getParams()));
  }

  /**
   * 添加查询列信息，可以逐个添加，也可以添加多个，用逗号隔开，如下用法都是合法的
   * <p>- <code>column("col1, col2, 'abc' as col3");</code>
   * <p>- <code>column("col1").column("col2").("col3, col4");</code>
   *
   * @param name
   * @return
   */
  public SelectSqlBuilder column(final String name) {
    columns.add(name);
    return this;
  }

  /**
   * 添加多个查询列，每个参数可以是一个col，可以是多个，如下用法都是合法的：
   * <p>- <code>columns("col1", "col2", "'abc' as col3"); </code>
   * <p>- <code>columns("col1, col2", "col3"); - columns("col1").clumns("col2", "col3")</code>
   *
   * @param names
   * @return
   */
  public SelectSqlBuilder columns(final String... names) {
    columns.addAll(Arrays.asList(names));
    return this;
  }

  /**
   * 添加分组表达式，如下用法都是合法的
   * <p>- <code>groupBy("col1", "col2");</code>
   * <p>- <code>groupBy("col1, col2");</code>
   * <p>- <code>groupBy("col1").groupBy("col2");</code>
   *
   * @param expr
   * @return
   */
  public SelectSqlBuilder groupBy(final String... expr) {
    groupBys.addAll(Arrays.asList(expr));
    return this;
  }

  /**
   * 添加 ORDER BY 排序表达式，如下用法都是合法的:
   * <p>- orderBy("col1 desc", "col2");
   * <p>- orderBy("col1, col2");
   * <p>- orderBy("col1").orderBy("col2 desc");
   *
   * @param expr
   * @return
   */
  public SelectSqlBuilder orderBy(final String... expr) {
    if (expr != null) {
      orderBys.addAll(Arrays.asList(expr));
    }
    return this;
  }

  /**
   * 添加 ORDER BY 排序表达式，并指明是否 ASC 升序，如下用法都是合法的:
   * <p>- orderBy("col1", true); 则为 ORDER BY col1 ASC
   * <p>- orderBy("col2", false); 则为 ORDER BY col2 DESC
   *
   * @param name      Name of the column by which to sort.
   * @param ascending If true, specifies the direction "asc", otherwise, specifies
   * <p>                 the direction "desc".
   */
  public SelectSqlBuilder orderBy(final String name, final boolean ascending) {
    if (ascending) {
      orderBys.add(name + " ASC");
    } else {
      orderBys.add(name + " DESC");
    }
    return this;
  }

  /**
   * having条件表达式
   * <p>- having("cnt > 10");
   * <p>多个条件用 AND 连接
   *
   * @param expr
   * @return
   */
  public SelectSqlBuilder having(final String expr) {
    havings.add(expr);
    return this;
  }

  /**
   * having条件表达式，带参数.
   * <p>- <code>having("cnt > ? or type = ?", n, type);</code>
   * <p>多个条件用 AND 连接
   *
   * @param expr
   * @param values
   * @return
   */
  public SelectSqlBuilder having(final String expr, final Object... values) {
    having(expr);
    if (values != null) {
      havingParams.addAll(Arrays.asList(values));
    }
    return this;
  }

  /**
   * 与另一个查询，做 UNION 连接
   * <p>若使用 UNION ALL 请使用 {@link #unionAll(SelectSqlBuilder)}
   * @param selectSql
   * @return
   */
  public SelectSqlBuilder union(final SelectSqlBuilder selectSql) {
    unions.add(new JoinEntity<>("\nUNION\n", selectSql));
    return this;
  }

  /**
   * 与另一个查询，做 UNION ALL 连接
   * <p>若使用 UNION 请使用 {@link #union(SelectSqlBuilder)}
   *
   * @param selectSql
   * @return
   */
  public SelectSqlBuilder unionAll(final SelectSqlBuilder selectSql) {
    unions.add(new JoinEntity<>("\nUNION ALL\n", selectSql));
    return this;
  }

  /**
   * JOIN 内连接表，表达式，如下写法都是合法的：
   * <p>- <code>join("table2")</code>
   * <p>- <code>join("table2 AS t2")</code>
   * <p>- <code>join("table2 AS t2 ON t1.id = t2.classId")</code>
   *
   * @param joinExpr
   * @return
   */
  public SelectSqlBuilder join(final String joinExpr) {
    joins.add(new JoinEntity<>("JOIN", joinExpr));
    return this;
  }

  /**
   * JOIN 内连接表，并给表指定别名：
   * <p>- <code>join("table2", "t2")</code>
   * <p>生成的sql为：<code>JOIN table2 AS t2</code>
   *
   * @param table
   * @param alias
   * @return
   */
  public SelectSqlBuilder join(final String table, final String alias) {
    return join(String.format("%s AS %s", table, alias));
  }

  /**
   * JOIN 内连接表，并给表指定别名和连接条件表达式：
   * <p>- <code>join("table2", "t2", "t1.id=t2.classId AND t2.status=2")</code>
   * <p>生成的sql为：<code>JOIN table2 AS t2 ON  AND t2.status=2</code>
   *
   * @param table
   * @param alias
   * @param onExpr
   * @return
   */
  public SelectSqlBuilder join(final String table, final String alias, final String onExpr) {
    return join(String.format("%s AS %s ON %s", table, alias, onExpr));
  }

  /**
   * LEFT JOIN 左连接表，表达式，如下写法都是合法的：
   * <p>- <code>leftJoin("table2")</code>
   * <p>- <code>leftJoin("table2 AS t2")</code>
   * <p>- <code>leftJoin("table2 AS t2 ON t1.id = t2.classId")</code>
   *
   * @param joinExpr
   * @return
   */
  public SelectSqlBuilder leftJoin(final String joinExpr) {
    joins.add(new JoinEntity<>("LEFT JOIN", joinExpr));
    return this;
  }

  /**
   * LEFT JOIN 左连接表，并给表指定别名：
   * <p>- <code>leftJoin("table2", "t2")</code>
   * <p>生成的sql为：<code>LEFT JOIN table2 AS t2</code>
   *
   * @param table
   * @param alias
   * @return
   */
  public SelectSqlBuilder leftJoin(final String table, final String alias) {
    return leftJoin(String.format("%s AS %s", table, alias));
  }

  /**
   * LEFT JOIN 左连接表，并给表指定别名和连接条件表达式：
   * <p>- <code>leftJoin("table2", "t2", "t1.id=t2.classId AND t2.status=2")</code>
   * <p>生成的sql为：<code>LEFT JOIN table2 AS t2 ON  AND t2.status=2</code>
   *
   * @param table
   * @param alias
   * @param onExpr
   * @return
   */
  public SelectSqlBuilder leftJoin(final String table, final String alias, final String onExpr) {
    return leftJoin(String.format("%s AS %s ON %s", table, alias, onExpr));
  }

  /**
   * RIGHT JOIN 右连接表，表达式，如下写法都是合法的：
   * <p>- <code>rightJoin("table2")</code>
   * <p>- <code>rightJoin("table2 AS t2")</code>
   * <p>- <code>rightJoin("table2 AS t2 ON t1.id = t2.classId")</code>
   *
   * @param joinExpr
   * @return
   */
  public SelectSqlBuilder rightJoin(final String joinExpr) {
    joins.add(new JoinEntity<>("RIGHT JOIN", joinExpr));
    return this;
  }

  /**
   * RIGHT JOIN 左连接表，并给表指定别名：
   * <p>- <code>rightJoin("table2", "t2")</code>
   * <p>生成的sql为：<code>RIGHT JOIN table2 AS t2</code>
   *
   * @param table
   * @param alias
   * @return
   */
  public SelectSqlBuilder rightJoin(final String table, final String alias) {
    return rightJoin(String.format("%s AS %s", table, alias));
  }

  /**
   * RIGHT JOIN 右连接表，并给表指定别名和连接条件表达式：
   * <p>- <code>rightJoin("table2", "t2", "t1.id=t2.classId AND t2.status=2")</code>
   * <p>生成的sql为：<code>RIGHT JOIN table2 AS t2 ON  AND t2.status=2</code>
   *
   * @param table
   * @param alias
   * @param onExpr
   * @return
   */
  public SelectSqlBuilder rightJoin(final String table, final String alias, final String onExpr) {
    return rightJoin(String.format("%s AS %s ON %s", table, alias, onExpr));
  }

  /**
   * CROSS JOIN 交叉连接
   * @param joinExpr
   * @return
   */
  public SelectSqlBuilder crossJoin(final String joinExpr) {
    joins.add(new JoinEntity<>("CROSS JOIN", joinExpr));
    return this;
  }

  /**
   * 声明为distinct查询
   *
   * @return
   */
  public SelectSqlBuilder distinct() {
    this.distinct = true;
    return this;
  }

  /**
   * 使用 LIMIT 限制查询条数，生成的SQL语句如下：
   * <p><code>LIMIT [limit]</code>
   * @param limit
   * @return
   */
  public SelectSqlBuilder limit(final int limit) {
    return this.limit(limit, 0);
  }

  /**
   * 使用 LIMIT 限制查询条数，生成的SQL语句如下：
   * <p><code>LIMIT [offset], [limit]</code>
   * <p>使用 OFFSET 关键字，请用 {@link #limitOffset(int, long)}
   *
   * @param limit
   * @param offset
   * @return
   */
  public SelectSqlBuilder limit(final int limit, final long offset) {
    this.limit = limit;
    this.offset = offset;
    return this;
  }

  @Override
  public String toString() {
    return this.getSql();
  }

  @Override
  public String getSql() {
    return getSql(false);
  }

  private String getSql(boolean isCount) {
    final StringBuilder sql = new StringBuilder(64);

    if (withSql != null) {
      sql.append("WITH ").append(withName).append(" AS (\n").append(withSql.getSql()).append("\n)\n");
    }

    sql.append("SELECT ");
    appendColumn(sql, isCount);

    appendList(sql, tables, " FROM ", ", ");
    appendList(sql, joins, " ", " ");
    appendList(sql, whereColumns, " WHERE ", " ");
    appendList(sql, groupBys, " GROUP BY ", ", ");
    appendList(sql, havings, " HAVING ", " AND ");
    appendList(sql, unions, "  ", " \n ");

    if (isCount) {
      return sql.toString();
    }

    appendList(sql, orderBys, " ORDER BY ", ", ");

    if (limit > 0) {
      sql.append(" ").append(dialect.limitOffset(limit, offset));
    }

    return sql.toString();
  }

  private void appendColumn(StringBuilder sql, boolean isCount) {
    if (distinct && !isCount) {
      sql.append("DISTINCT ");
    }
    if (isCount) {
      sql.append("count(1) AS cnt");
    } else if (columns.isEmpty()) {
      sql.append("*");
    } else {
      appendList(sql, columns, "", ", ");
    }
  }

  public String getCountSql() {
    return getSql(true);
  }

  @Override
  public Object[] getParams() {
    Stream<Object[]> paramsArr = Stream.of(withSql == null ? EMPTY_PARAMS : withSql.getParams(),
      whereParams.toArray(), havingParams.toArray());
    paramsArr = Stream.concat(paramsArr, unions.stream().map(v -> v.expr.getParams()));

    return paramsArr.flatMap(Arrays::stream).toArray();
  }

  private static class JoinEntity<V> {
    String op;
    V expr;

    /**
     * @param op
     * @param expr
     */
    public JoinEntity(final String op, final V expr) {
      this.op = op;
      this.expr = expr;
    }

    @Override
    public String toString() {
      return op + " " + expr.toString();
    }
  }

}
