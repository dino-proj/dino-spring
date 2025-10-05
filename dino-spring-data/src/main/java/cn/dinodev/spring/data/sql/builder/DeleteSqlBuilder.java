package cn.dinodev.spring.data.sql.builder;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * SQL DELETE语句构建器
 * 用于构建DELETE SQL语句的Builder类，支持WHERE条件等子句
 *
 * @author Cody Lu
 * @date 2022-03-07 19:25:16
 */

public final class DeleteSqlBuilder extends WhereSql<DeleteSqlBuilder> {

  /**
   * 根据表名创建DeleteSqlBuilder实例, 如下写法都是合法的：
   * <p>- <code>"table1"</code>
   * <p>- <code>"table1 as t1"</code>
   *
   * @param table 表名
   * @return DeleteSqlBuilder实例
   */
  public static DeleteSqlBuilder create(final String table) {
    final DeleteSqlBuilder builder = new DeleteSqlBuilder();
    builder.setThat(builder);
    builder.tables.add(table);
    return builder;
  }

  /**
   * 创建DeleteSqlBuilder实例并设置表名和别名
   * <p>- <code>生成的sql片段为：table AS alias</code>
   *
   * @param table 表名
   * @param alias 别名
   * @return DeleteSqlBuilder实例
   */
  public static DeleteSqlBuilder create(final String table, final String alias) {
    final DeleteSqlBuilder builder = new DeleteSqlBuilder();
    builder.setThat(builder);
    builder.tables.add(table + " AS " + alias);
    return builder;
  }

  /**
   * 私有构造函数，防止直接实例化
   */
  private DeleteSqlBuilder() {
    // 私有构造函数，使用静态工厂方法创建实例
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
