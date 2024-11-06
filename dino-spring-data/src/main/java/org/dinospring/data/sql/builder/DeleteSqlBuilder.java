package org.dinospring.data.sql.builder;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:25:16
 */

public class DeleteSqlBuilder extends WhereSql<DeleteSqlBuilder> {
  /**
   * 根据表名构造, 如下写法都是合法的：
   * <p>- <code>"table1"</code>
   * <p>- <code>"table1 as t1"</code>
   *
   * @param table
   */
  public DeleteSqlBuilder(final String table) {
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
  public DeleteSqlBuilder(final String table, final String alias) {
    setThat(this);
    this.table(table + " AS " + alias);
  }

  @Override
  public String getSql() {
    final StringBuilder sql = new StringBuilder(64);
    if (withSql != null) {
      sql.append("WITH ").append(withName).append(" AS (\n").append(withSql.getSql()).append("\n)\n");
    }
    sql.append("DELETE FROM ");
    appendList(sql, tables, " ", ", ");
    appendList(sql, whereColumns, " WHERE ", " ");
    return sql.toString();
  }

  @Override
  public Object[] getParams() {

    Stream<Object[]> paramsArr = Stream.of(withSql == null ? EMPTY_PARAMS : withSql.getParams(),
        whereParams.toArray());

    return paramsArr.flatMap(Arrays::stream).toArray();
  }

  @Override
  public String toString() {
    return this.getSql();
  }
}
