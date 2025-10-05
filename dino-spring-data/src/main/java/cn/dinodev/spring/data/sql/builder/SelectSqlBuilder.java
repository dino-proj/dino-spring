// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.sql.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import cn.dinodev.spring.data.sql.dialect.Dialect;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:21:00
 */

public final class SelectSqlBuilder extends WhereSql<SelectSqlBuilder> {

  private static final String TABLE_ALIAS_FORMAT = "%s AS %s";
  private static final String TABLE_ALIAS_ON_FORMAT = "%s AS %s ON %s";

  private boolean distinctFlag;

  private final List<String> columnsList = new ArrayList<>();

  private final List<JoinEntity<String>> joins = new ArrayList<>();

  private final List<String> groupBysList = new ArrayList<>();

  private final List<String> havingsList = new ArrayList<>();

  private final List<JoinEntity<SelectSqlBuilder>> unions = new ArrayList<>();

  private final List<String> orderBysList = new ArrayList<>();

  private final List<Object> havingParams = new ArrayList<>();

  private final List<Object> joinParams = new ArrayList<>();

  private int limitValue;

  private long offset;

  private final Dialect dialect;

  /**
   * 私有构造函数，防止直接实例化
   * @param dialect 数据库方言
   */
  private SelectSqlBuilder(final Dialect dialect) {
    this.dialect = dialect;
  }

  /**
   * 私有构造函数，防止直接实例化
   * @param dialect 数据库方言
   * @param table 表名
   */
  private SelectSqlBuilder(final Dialect dialect, final String table) {
    this.dialect = dialect;
    this.tables.add(table);
  }

  /**
   * 私有构造函数，防止直接实例化
   * @param dialect 数据库方言
   * @param table 表名
   * @param alias 表别名
   */
  private SelectSqlBuilder(final Dialect dialect, final String table, final String alias) {
    this.dialect = dialect;
    this.tables.add(TABLE_ALIAS_FORMAT.formatted(table, alias));
  }

  /**
   * 私有构造函数，防止直接实例化
   * @param subQuery 子查询
   * @param alias 子查询别名
   */
  private SelectSqlBuilder(final SelectSqlBuilder subQuery, final String alias) {
    this.dialect = subQuery.dialect;
    this.tables.add(String.format("( %s ) AS %s", subQuery.getSql(), alias));
    this.whereParams.addAll(Arrays.asList(subQuery.getParams()));
  }

  /**
   * 创建SelectSqlBuilder实例
   * @param dialect 数据库方言
   * @return 配置好的SelectSqlBuilder实例
   */
  public static SelectSqlBuilder create(final Dialect dialect) {
    SelectSqlBuilder builder = new SelectSqlBuilder(dialect);
    builder.initializeBuilder();
    return builder;
  }

  /**
   * 根据表名创建SelectSqlBuilder实例
   * <p>支持的格式：
   * <p>- <code>"table1"</code>
   * <p>- <code>"table1, table2"</code>
   * <p>- <code>"table1 as t1"</code>
   * <p>- <code>"table1 as t1 join table2 as t2 on t1.id=t2.id"</code>
   *
   * @param dialect 数据库方言
   * @param table 表名
   * @return 配置好的SelectSqlBuilder实例
   */
  public static SelectSqlBuilder create(final Dialect dialect, final String table) {
    SelectSqlBuilder builder = new SelectSqlBuilder(dialect, table);
    builder.initializeBuilder();
    return builder;
  }

  /**
   * 根据表名和别名创建SelectSqlBuilder实例
   * <p>生成的sql片段为：table AS alias
   *
   * @param dialect 数据库方言
   * @param table 表名
   * @param alias 表别名
   * @return 配置好的SelectSqlBuilder实例
   */
  public static SelectSqlBuilder create(final Dialect dialect, final String table, final String alias) {
    SelectSqlBuilder builder = new SelectSqlBuilder(dialect, table, alias);
    builder.initializeBuilder();
    return builder;
  }

  /**
   * 根据子查询创建SelectSqlBuilder实例
   * @param subQuery 子查询
   * @param alias 子查询的别名
   * @return 配置好的SelectSqlBuilder实例
   */
  public static SelectSqlBuilder create(final SelectSqlBuilder subQuery, final String alias) {
    SelectSqlBuilder builder = new SelectSqlBuilder(subQuery, alias);
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
   * 添加查询列信息，可以逐个添加，也可以添加多个，用逗号隔开，如下用法都是合法的
   * <p>- <code>column("col1, col2, 'abc' as col3");</code>
   * <p>- <code>column("col1").column("col2").("col3, col4");</code>
   *
   * @param name
   * @return
   */
  public SelectSqlBuilder column(final String name) {
    columnsList.add(name);
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
    columnsList.addAll(Arrays.asList(names));
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
    groupBysList.addAll(Arrays.asList(expr));
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
      orderBysList.addAll(Arrays.asList(expr));
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
      orderBysList.add(name + " ASC");
    } else {
      orderBysList.add(name + " DESC");
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
    havingsList.add(expr);
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
    return join(String.format(TABLE_ALIAS_FORMAT, table, alias));
  }

  /**
   * JOIN 内连接表，并给表指定别名和连接条件表达式：
   * <p>- <code>join("table2", "t2", "t1.id=t2.classId AND t2.status=2")</code>
   * <p>生成的sql为：<code>JOIN table2 AS t2 ON  AND t2.status=2</code>
   *
   * @param table
   * @param alias
   * @param onExpr
   * @param values 参数
   * @return
   */
  public SelectSqlBuilder join(final String table, final String alias, final String onExpr, final Object... values) {
    join(String.format(TABLE_ALIAS_ON_FORMAT, table, alias, onExpr));
    if (values != null) {
      joinParams.addAll(Arrays.asList(values));
    }
    return this;
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
    return leftJoin(String.format(TABLE_ALIAS_FORMAT, table, alias));
  }

  /**
   * LEFT JOIN 左连接表，并给表指定别名和连接条件表达式：
   * <p>- <code>leftJoin("table2", "t2", "t1.id=t2.classId AND t2.status=2")</code>
   * <p>生成的sql为：<code>LEFT JOIN table2 AS t2 ON  AND t2.status=2</code>
   *
   * @param table
   * @param alias
   * @param onExpr
   * @param values 参数
   * @return
   */
  public SelectSqlBuilder leftJoin(final String table, final String alias, final String onExpr,
      final Object... values) {
    leftJoin(String.format(TABLE_ALIAS_ON_FORMAT, table, alias, onExpr));
    if (values != null) {
      joinParams.addAll(Arrays.asList(values));
    }
    return this;
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
    return rightJoin(String.format(TABLE_ALIAS_FORMAT, table, alias));
  }

  /**
   * RIGHT JOIN 右连接表，并给表指定别名和连接条件表达式：
   * <p>- <code>rightJoin("table2", "t2", "t1.id=t2.classId AND t2.status=2")</code>
   * <p>生成的sql为：<code>RIGHT JOIN table2 AS t2 ON  AND t2.status=2</code>
   *
   * @param table
   * @param alias
   * @param onExpr
   * @param values 参数
   * @return
   */
  public SelectSqlBuilder rightJoin(final String table, final String alias, final String onExpr,
      final Object... values) {
    rightJoin(String.format(TABLE_ALIAS_ON_FORMAT, table, alias, onExpr));
    if (values != null) {
      joinParams.addAll(Arrays.asList(values));
    }
    return this;
  }

  /**
   * CROSS JOIN 交叉连接,cross join不可以加on
   * <p>- <code>crossJoin("table2")</code>
   * <p>生成的sql为：<code>CROSS JOIN table2</code>
   * OR
   * <p>- <code>crossJoin("jsonb_array_elements(knowledge)")</code>
   * <p>生成的sql为：<code>CROSS JOIN jsonb_array_elements(knowledge)</code>
   *
   * @param joinExpr
   * @return
   */
  public SelectSqlBuilder crossJoin(final String joinExpr) {
    joins.add(new JoinEntity<>("CROSS JOIN", joinExpr));
    return this;
  }

  /**
   * CROSS JOIN 交叉连接,cross join不可以加on
   * <p>- <code>crossJoin("table2", "t2")</code>
   * <p>生成的sql为：<code>CROSS JOIN table2 AS t2</code>
   * OR
   * <p>- <code>crossJoin("jsonb_array_elements(knowledge)", "value")</code>
   * <p>生成的sql为：<code>CROSS JOIN jsonb_array_elements(knowledge) AS value</code>
   *
   * @param joinExpr
   * @param alias
   * @return
   */
  public SelectSqlBuilder crossJoin(final String joinExpr, final String alias) {
    return crossJoin(String.format(TABLE_ALIAS_FORMAT, joinExpr, alias));
  }

  /**
   * 声明为distinct查询
   *
   * @return
   */
  public SelectSqlBuilder distinct() {
    this.distinctFlag = true;
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
    this.limitValue = limit;
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
    appendList(sql, groupBysList, " GROUP BY ", ", ");
    appendList(sql, havingsList, " HAVING ", " AND ");
    appendList(sql, unions, "  ", " \n ");

    if (isCount) {
      return sql.toString();
    }

    appendList(sql, orderBysList, " ORDER BY ", ", ");

    if (limitValue > 0) {
      sql.append(' ').append(dialect.limitOffset(limitValue, offset));
    }

    return sql.toString();
  }

  private void appendColumn(StringBuilder sql, boolean isCount) {
    if (distinctFlag && !isCount) {
      sql.append("DISTINCT ");
    }
    if (isCount) {
      sql.append("count(1) AS cnt");
    } else if (columnsList.isEmpty()) {
      sql.append('*');
    } else {
      appendList(sql, columnsList, "", ", ");
    }
  }

  /**
   * 获取用于计数的SQL语句
   * @return 计数SQL语句字符串
   */
  public String getCountSql() {
    return getSql(true);
  }

  @Override
  public Object[] getParams() {
    Stream<Object[]> paramsArr = Stream.of(
        withSql == null ? EMPTY_PARAMS : withSql.getParams(),
        joinParams.toArray(),
        whereParams.toArray(),
        havingParams.toArray());
    paramsArr = Stream.concat(paramsArr, unions.stream().map(v -> v.expr.getParams()));

    return paramsArr.flatMap(Arrays::stream).toArray();
  }

  /**
   * 连接实体类，用于表示SQL连接操作
   */
  private static class JoinEntity<V> {
    private final String op;
    private final V expr;

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
