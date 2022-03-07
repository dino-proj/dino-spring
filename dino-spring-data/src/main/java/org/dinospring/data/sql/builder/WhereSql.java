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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.data.Range;
import org.dinospring.data.sql.Logic;
import org.dinospring.data.sql.Oper;
import org.dinospring.data.sql.SqlBuilder;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 19:20:18
 */

public abstract class WhereSql<T extends SqlBuilder> implements SqlBuilder {
  protected static final Object[] EMPTY_PARAMS = new Object[0];

  protected List<String> tables = new ArrayList<>();

  protected List<String> whereColumns = new ArrayList<>();

  protected List<Object> whereParams = new ArrayList<>();

  protected SqlBuilder withSql = null;

  protected String withName = null;

  private T that;

  protected void setThat(T that) {
    this.that = that;
  }

  /**
   * 设置要查询的表，可以是多个表，如下用法都是正确的：
   * <p>- <code>from("table1, table2"); </code>
   * <p>- <code>from("table1", "table2");</code>
   *
   * @param tables
   * @return
   */
  public T table(final String... tables) {
    if (tables != null) {
      this.tables.addAll(Arrays.asList(tables));
    }
    return that;
  }

  /**
   * with 语句支持， 并将子查询自动添加到from中：
   * <p>- <code>with #alias AS ( #subQuery语句 ); select colums from #alias</code>
   *
   * @param subQuery 子查询
   * @param alias 临时查询的别名
   * @return
   */
  public T with(final SqlBuilder subQuery, final String alias) {
    this.withName = alias;
    this.withSql = subQuery;
    this.table(alias);
    return that;
  }

  /**
   * where 表达式，如下写法都是合法的：
   * <p>- <code>where("status = 1")</code>
   * <p>- <code>where("status = 1 and id = ?")</code>
   *
   * @param expr
   * @return
   */
  public T where(final String expr) {
    whereColumns.add(expr);
    return that;
  }

  /**
   * where 表达式，带参数值，如下写法都是合法的：
   * <p>- <code>where("id = ?", id)</code>
   * <p>- <code>where("status = 1 and classId = ? and score > ?", classId, 60)</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   *
   * @param expr
   * @param values
   * @return
   */
  public T where(final String expr, final Object... values) {
    whereColumns.add(expr);
    whereParams.addAll(Arrays.asList(values));
    return that;
  }

  /**
   * where 表达式，例如：
   * <p>- <code>where("col1", Oper.EQ, val)</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T where(final String column, final Oper op, final Object value) {
    where(op.makeExpr(column));
    whereParams.add(value);
    return that;
  }

  /**
   * where 表达式，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>whereIfNotNull("type = ?", null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>whereIfNotNull("type = ?", 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   *
   * @param expr
   * @param value
   * @return
   */
  public T whereIfNotNull(final String expr, final Object value) {
    if (value == null) {
      return that;
    }

    where(expr, value);
    return that;
  }

  /**
   * where 表达式，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>whereIfNotNull("type", Oper.EQ, null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>whereIfNotNull("type", Oper.EQ, 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T whereIfNotNull(final String column, final Oper op, final Object value) {
    if (!Objects.isNull(value)) {
      where(column, op, value);
    }
    return that;
  }

  /**
   * where 表达式，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>whereIf(false, "type=?", 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>whereIf(true, "type=? or type=?", 1, 2); type= 1||2的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   *
   * @param cnd
   * @param expr
   * @param values
   * @return
   */
  public T whereIf(final boolean cnd, final String expr, final Object... values) {
    if (!cnd) {
      return that;
    }
    return where(expr, values);
  }

  /**
   * where 表达式，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>whereIf(false, "type", “=”, 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>whereIf(true, "type", Oper.EQ, 1, 2); type= 1||2的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   *
   * @param cnd
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T whereIf(final boolean cnd, final String column, final Oper op, final Object value) {
    if (!cnd) {
      return that;
    }

    return where(column, op, value);
  }

  /**
   * where 表达式，用 AND 连接，如下写法都是合法的：
   * <p>- <code>and("status = 1")</code>
   * <p>- <code>and("status = 1 and id = ?")</code>
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param expr
   * @return
   */
  public T and(final String expr) {
    appendWhere(Logic.AND, expr);
    return that;
  }

  /**
   * where 表达式，带参数值，用 AND 连接，如下写法都是合法的：
   * <p>- <code>and("id = ?", id)</code>
   * <p>- <code>and("status = 1 and classId = ? and score > ?", classId, 60)</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param expr
   * @param values
   * @return
   */
  public T and(final String expr, final Object... values) {
    and(expr);
    whereParams.addAll(Arrays.asList(values));
    return that;
  }

  /**
   * where 表达式，用 AND 连接，例如：
   * <p>- <code>and("col1", Oper.EQ, val)</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T and(final String column, final Oper op, final Object value) {
    return and(String.format("%s %s ?", column, op.getOp()), value);
  }

  /**
   * where 表达式，用 AND 连接，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>andIf(false, "type=?", 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>andIf(true, "type=? or type=?", 1, 2); type= 1||2的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List, String)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param cnd
   * @param expr
   * @param values
   * @return
   */
  public T andIf(final boolean cnd, final String expr, final Object... values) {
    if (!cnd) {
      return that;
    }

    return and(expr, values);
  }

  /**
   * where 表达式，用 AND 连接，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>andIf(false, "type", “=”, 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>andIf(true, "type", Oper.EQ, 1); type= 1的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param cnd
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T andIf(final boolean cnd, final String column, final Oper op, final Object value) {
    if (!cnd) {
      return that;
    }

    return and(column, op, value);
  }

  /**
   * where 表达式，用 AND 连接，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>andIfNotNull("type = ?", null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>andIfNotNull("type = ?", 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param expr
   * @param value
   * @return
   */

  public T andIfNotNull(final String expr, final Object value) {
    return andIf(!Objects.isNull(value), expr, value);
  }

  /**
   * where 表达式，用 AND 连接，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>andIfNotNull("type", Oper.EQ, null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>andIfNotNull("type", Oper.EQ, 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T andIfNotNull(final String column, final Oper op, final Object value) {
    return andIf(!Objects.isNull(value), column, op, value);
  }

  /**
   * where eq 表达式，用 AND 连接，例如：
   * <p>- <code>and("col1", val)</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T eq(final String column, final Object value) {
    return and(String.format("%s = ?", column), value);
  }

  /**
   * where eq 表达式，用 AND 连接，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>eqIf(false, "type", 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>eqIf(true, "type", 1); type= 1的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param cnd
   * @param column
   * @param value
   * @return
   */
  public T eqIf(final boolean cnd, final String column, final Object value) {
    return andIf(cnd, column, Oper.EQ, value);
  }

  /**
   * where eq 表达式，用 AND 连接，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>eqIfNotNull("type", null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>eqIfNotNull("type", 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param column
   * @param value
   * @return
   */
  public T eqIfNotNull(final String column, final Object value) {
    return andIf(!Objects.isNull(value), column, Oper.EQ, value);
  }

  /**
   * where eq 表达式，用 AND 连接，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>eqIfNotNull("type", null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>eqIfNotNull("type", 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 AND 会被忽略
   *
   * @param column
   * @param value
   * @return
   */
  //TODO
  public T eqIfNotBlank(final String column, final String value) {
    return andIf(StringUtils.isNotBlank(value), column, Oper.EQ, value);
  }

  /**
   * where 表达式，用OR连接，如下写法都是合法的：
   * <p>- <code>or("status = 1")</code>
   * <p>- <code>or("status = 1 and id = ?")</code>
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param expr
   * @return
   */
  public T or(final String expr) {
    appendWhere(Logic.OR, expr);
    return that;
  }

  /**
   * where 表达式，带参数值，用 OR 连接，如下写法都是合法的：
   * <p>- <code>or("id = ?", id)</code>
   * <p>- <code>or("status = 1 and classId = ? and score > ?", classId, 60)</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param expr
   * @param values
   * @return
   */
  public T or(final String expr, final Object... values) {
    or(expr);
    whereParams.addAll(Arrays.asList(values));
    return that;
  }

  /**
   * where 表达式，用 OR 连接，例如：
   * <p>- <code>or("col1", Oper.EQ, val)</code>
   * <p>in表达式请使用 {@link #in(String, List, String)}
   * <p>like表达式请使用 {@link #like(String, String, String)}
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T or(final String column, final Oper op, final Object value) {
    or(op.makeExpr(column));
    whereParams.add(value);
    return that;
  }

  /**
   * where 表达式，用 OR 连接，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>orIf(false, "type=?", 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>orIf(true, "type=? or type=?", 1, 2); type= 1||2的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List, String)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param cnd
   * @param expr
   * @param values
   * @return
   */
  public T orIf(final boolean cnd, final String expr, final Object... values) {
    if (!cnd) {
      return that;
    }

    return or(expr, values);
  }

  /**
   * where 表达式，用 OR 连接，根据传入条件，当为false时，则忽略此查询条件
   * <p>- <code>orIf(false, "type", “=”, 1); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>orIf(true, "type", Oper.EQ, 1, 2); type= 1||2的记录会被筛选出来。</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param cnd
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T orIf(final boolean cnd, final String column, final Oper op, final Object value) {
    if (!cnd) {
      return that;
    }

    return or(column, op, value);
  }

  /**
   * where 表达式，用 OR 连接，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>orIfNotNull("type = ?", null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>orIfNotNull("type = ?", 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param expr
   * @param value
   * @return
   */
  public T orIfNotNull(final String expr, final Object value) {
    return orIf(!Objects.isNull(value), expr, value);
  }

  /**
   * where 表达式，用 OR 连接，判断值不为null时，表达式才被采用，否则表达式会被丢弃：
   * <p>- <code>orIfNotNull("type", Oper.EQ, null); 则会忽略这个表达式，不会根据type字段筛选。</code>
   * <p>- <code>orIfNotNull("type", Oper.EQ, 1);</code>
   * <p>in表达式请使用 {@link #in(String, List)}
   * <p>like表达式请使用 {@link #like(String, String)}
   * <p>如果前面没有其他表达式，则 OR 会被忽略
   *
   * @param column
   * @param op
   * @param value
   * @return
   */
  public T orIfNotNull(final String column, final Oper op, final Object value) {
    return orIf(!Objects.isNull(value), column, op, value);
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>like("name", "abc");生成的sql为：</code>
   * <p>[AND] name like '%abc%';
   *
   * <p>默认使用 AND 连接
   * <p>部分匹配请使用 {@link #startWith(String, String)} 或 {@link #endWith(String, String)}
   *
   * @param column
   * @param start
   * @param end
   * @return
   */
  //TODO 注释
  public T between(final String column, final Number start, Number end) {
    if (!Objects.isNull(start)) {
      this.and(column, Oper.GTE, start);
    }
    if (!Objects.isNull(end)) {
      this.and(column, Oper.LTE, end);
    }
    return that;
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>like("name", "abc");生成的sql为：</code>
   * <p>[AND] name like '%abc%';
   *
   * <p>默认使用 AND 连接
   * <p>部分匹配请使用 {@link #startWith(String, String)} 或 {@link #endWith(String, String)}
   *
   * @param column
   * @param start
   * @param end
   * @return
   */
  //TODO 注释
  public T between(final String column, final Range<?> range) {
    if (!Objects.isNull(range.getBegin())) {
      this.and(column, Oper.GTE, range.getBegin());
    }
    if (!Objects.isNull(range.getEnd())) {
      this.and(column, Oper.LTE, range.getEnd());
    }
    return that;
  }

  /**
   * where 表达式中的 ANY 函数，其参数为一个子查询，如下：
   * <p>- <code>any("id", "select id from student where classId=1"),生成的sql为：</code>
   * <p>[AND] id = any(subquery sql)
   * <p>默认使用 AND 连接
   *
   * @param column
   * @param subQuery
   * @return
   */
  public T any(final String column, final T subQuery) {
    return any(column, subQuery, Logic.AND);
  }

  /**
   * where 表达式中的 ANY 函数，其参数为一个子查询，如下：
   * <p>- <code>any("id", "select id from student where classId=1", "OR"),生成的sql为：</code>
   * <p>[OR] id = any(subquery sql)
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   *
   * @param column
   * @param subQuery
   * @param logic
   * @return
   */
  public T any(final String column, final T subQuery, final Logic logic) {
    appendWhere(logic, String.format("%s = any(%s)", column, subQuery.getSql()));
    whereParams.addAll(Arrays.asList(subQuery.getParams()));
    return that;
  }

  /**
   * 任意列LIKE %value%：
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 some(["col1", "col2"], Oper.EQ "a", Logic.AND) 则转化为 AND ( col1 = 'a' OR col2 = 'a')</p>
   * <p>- 如 some(["col1", "col2"], Oper.EQ, "a", Logic.OR) 则转化为 OR ( col1 = 'a' OR col2 = 'a')</p>
   * @param columns 多个列
   * @param op 操作符
   * @param value 关键字
   * @param logic 外围逻辑
   * @return
   */
  public T some(final String[] columns, final Oper op, final Object value, final Logic logic) {
    if (Objects.isNull(value)) {
      if (op == Oper.EQ) {
        appendNColumnExpr(logic, columns, Oper.IS_NULL, null, Logic.OR);
      } else if (op == Oper.NE) {
        appendNColumnExpr(logic, columns, Oper.IS_NOT_NULL, null, Logic.OR);
      }
    } else {
      appendNColumnExpr(logic, columns, op, value, Logic.OR);
    }
    return that;
  }

  /**
   * AND 任意列LIKE %value%:
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 some(["col1", "col2"], Oper.EQ, "a") 则转化为 AND ( col1 = 'a' OR col2 = 'a')</p>
   * @param columns 多个列
   * @param op 操作符
   * @param value 关键字
   * @return
   */
  public T some(final String[] columns, final Oper op, final Object value) {
    return some(columns, op, value, Logic.AND);
  }

  /**
   * 任意列LIKE %value%：
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 some(["col1", "col2"], Oper.EQ "a", Logic.AND) 则转化为 AND ( col1 = 'a' OR col2 = 'a')</p>
   * <p>- 如 some(["col1", "col2"], Oper.EQ, "a", Logic.OR) 则转化为 OR ( col1 = 'a' OR col2 = 'a')</p>
   * @param cnd 条件
   * @param columns 多个列
   * @param op 操作符
   * @param value 关键字
   * @param logic 外围逻辑
   * @return
   */
  public T someIf(final boolean cnd, final String[] columns, final Oper op, final Object value, final Logic logic) {
    if (cnd) {
      appendNColumnExpr(logic, columns, op, value, Logic.OR);
    }
    return that;
  }

  /**
   * AND 任意列 [op] [value]:
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 some(["col1", "col2"], Oper.EQ, "a") 则转化为 AND ( col1 = 'a' OR col2 = 'a')</p>
   * @param cnd 条件
   * @param columns 多个列
   * @param op 操作符
   * @param value 关键字
   * @return
   */
  public T someIf(final boolean cnd, final String[] columns, final Oper op, final String value) {
    if (!cnd) {
      return that;
    }
    return some(columns, op, value, Logic.AND);
  }

  /**
   * where 表达式中的 IN 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用=操作符替代</code>
   * <p>- <code>in("type", typesList);</code>
   * <p>默认使用 AND 连接
   *
   * @param column
   * @param values
   * @return
   */
  public T in(final String column, final List<?> values) {
    return in(column, values, Logic.AND);
  }

  /**
   * where 表达式中的 IN 语句，根据传入条件，当为false时，则忽略此查询条件.
   * 其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用=操作符替代</code>
   * <p>- <code>inIf(true, "type", typesList);</code>
   * <p>默认使用 AND 连接
   *
   * @param cnd
   * @param column
   * @param values
   * @return
   */
  public T inIf(final boolean cnd, final String column, final List<?> values) {
    return cnd ? in(column, values, Logic.AND) : that;
  }

  /**
   * where 表达式中的 IN 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用=操作符替代</code>
   * <p>- <code>in("type", typesList, "OR");</code>
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   *
   * @param column
   * @param values
   * @param logic
   * @return
   */
  public T in(final String column, final List<?> values, final Logic logic) {
    if (values == null || values.isEmpty()) {
      return that;
    }
    if (values.size() == 1) {
      appendWhere(logic, column + " = ?");
    } else {
      appendWhere(logic, makeINExpr(column, "IN", values.size()));
    }
    whereParams.addAll(values);
    return that;
  }

  /**
   * where 表达式中的 IN 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用=操作符替代</code>
   * <p>- <code>in("type", typesArray);</code>
   * <p>默认使用 AND 连接
   *
   * @param column
   * @param values
   * @return
   */
  public T in(final String column, final Object[] values) {
    return in(column, values, Logic.AND);
  }

  /**
   * where 表达式中的 IN 语句，根据传入条件，当为false时，则忽略此查询条件.
   * 其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用=操作符替代</code>
   * <p>- <code>inIf(true, "type", typesArray);</code>
   * <p>默认使用 AND 连接
   *
   * @param cnd
   * @param column
   * @param values
   * @return
   */
  public T inIf(final boolean cnd, final String column, final Object[] values) {
    return cnd ? in(column, values, Logic.AND) : that;
  }

  /**
   * where 表达式中的 IN 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用=操作符替代</code>
   * <p>- <code>in("type", typesArray, "OR");</code>
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   *
   * @param column
   * @param values
   * @param logic
   * @return
   */
  public T in(final String column, final Object[] values, final Logic logic) {
    if (values == null || values.length == 0) {
      return that;
    }
    return in(column, Arrays.asList(values), logic);
  }

  /**
   * where 表达式中的 NOT IN 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用!=操作符替代</code>
   * <p>- <code>notIn("type", typesArray);</code>
   * <p>默认使用 AND 连接
   *
   * @param column
   * @param values
   * @return
   */
  public T notIn(final String column, final Object[] values) {
    return notIn(column, values, Logic.AND);
  }

  /**
   * where 表达式中的 IN 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果数组为空，则会忽略此条件</code>
   * <p>- <code>如果数组长度为1，则用!=操作符替代</code>
   * <p>- <code>notIn("type", typesArray, "OR");</code>
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   *
   * @param column
   * @param values
   * @param logic
   * @return
   */
  public T notIn(final String column, final Object[] values, final Logic logic) {
    if (values == null || values.length == 0) {
      return that;
    }
    if (values.length == 1) {
      appendWhere(logic, Oper.NE.makeExpr(column));
    } else {
      appendWhere(logic, makeINExpr(column, "NOT IN", values.length));
    }
    whereParams.addAll(Arrays.asList(values));
    return that;
  }

  /**
   * [AND] column IS NULL
   * @param column 列名
   * @return
   */
  public T isNull(final String column) {
    return isNull(column, Logic.AND);
  }

  /**
   * [AND|OR] column IS NULL
   * @param column 列名
   * @param logic 外部逻辑符
   * @return
   */
  public T isNull(final String column, final Logic logic) {
    appendWhere(logic, Oper.IS_NULL.makeExpr(column));
    return that;
  }

  /**
   * 如果条件成立，则为[AND] column IS NULL，否则忽略
   * @param cnd 条件
   * @param column 列名
   * @return
   */
  public T isNullIf(final boolean cnd, final String column) {
    if (!cnd) {
      return that;
    }
    return isNull(column, Logic.AND);
  }

  /**
   * 如果条件成立，则为[AND|OR] column IS NULL，否则忽略
   * @param cnd 条件
   * @param column 列名
   * @param logic 外部逻辑符
   * @return
   */
  public T isNullIf(final boolean cnd, final String column, final Logic logic) {
    if (!cnd) {
      return that;
    }
    appendWhere(logic, Oper.IS_NULL.makeExpr(column));
    return that;
  }

  /**
   * [AND] column IS NOT NULL
   * @param column 列名
   * @return
   */
  public T isNotNull(final String column) {
    return isNotNull(column, Logic.AND);
  }

  /**
   * [AND|OR] column IS NOT NULL
   * @param column 列名
   * @param logic 外部逻辑符
   * @return
   */
  public T isNotNull(final String column, final Logic logic) {
    appendWhere(logic, Oper.IS_NOT_NULL.makeExpr(column));
    return that;
  }

  /**
   * 如果条件成立，则为[AND] column IS NOT NULL，否则忽略
   * @param cnd 条件
   * @param column 列名
   * @return
   */
  public T isNotNullIf(final boolean cnd, final String column) {
    if (!cnd) {
      return that;
    }
    return isNotNull(column, Logic.AND);
  }

  /**
   * 如果条件成立，则为[AND|OR] column IS NOT NULL，否则忽略
   * @param cnd 条件
   * @param column 列名
   * @param logic 外部逻辑符
   * @return
   */
  public T isNotNullIf(final boolean cnd, final String column, final Logic logic) {
    if (!cnd) {
      return that;
    }
    appendWhere(logic, Oper.IS_NOT_NULL.makeExpr(column));
    return that;
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>like("name", "abc");生成的sql为：</code>
   * <p>[AND] name like '%abc%';
   *
   * <p>默认使用 AND 连接
   * <p>部分匹配请使用 {@link #startWith(String, String)} 或 {@link #endWith(String, String)}
   *
   * @param column
   * @param value
   * @return
   */
  public T like(final String column, final String value) {
    return like(column, value, Logic.AND);
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>like("name", "abc", "OR");生成的sql为：</code>
   * <p>[OR] name like '%abc%';
   *
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   * <p>部分匹配请使用 {@link #startWith(String, String)} 或 {@link #endWith(String, String)}
   *
   * @param column
   * @param value
   * @param logic
   * @return
   */
  public T like(final String column, final String value, final Logic logic) {
    if (StringUtils.isBlank(value)) {
      appendOrTrue(logic);
      return that;
    }
    appendWhere(logic, column + " like ?");
    whereParams.add("%" + value + "%");
    return that;
  }

  /**
   * 任意列LIKE %value%：
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 someLike(["col1", "col2", "a", Logic.AND]) 则转化为 AND ( col1 LIKE '%a%' OR col2 LIKE '%a%')</p>
   * <p>- 如 someLike(["col1", "col2", "a", Logic.OR]) 则转化为 OR ( col1 LIKE '%a%' OR col2 LIKE '%a%')</p>
   * @param columns 多个列
   * @param value 关键字
   * @param logic 外围逻辑
   * @return
   */
  public T someLike(final String[] columns, final String value, final Logic logic) {
    if (StringUtils.isBlank(value)) {
      appendOrTrue(logic);
      return that;
    }
    appendNColumnExpr(logic, columns, Oper.LIKE, "%" + value + "%", Logic.OR);

    return that;
  }

  /**
   * AND 任意列LIKE %value%:
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>如 someLike(["col1", "col2", "a") 则转化为 AND ( col1 LIKE '%a%' OR col2 LIKE '%a%')</p>
   * @param columns 多个列
   * @param value 关键字
   * @return
   */
  public T someLike(final String[] columns, final String value) {
    return someLike(columns, value, Logic.AND);
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>startWith("name", "abc");生成的sql为：</code>
   * <p>[AND] name like 'abc%';
   *
   * <p>默认使用 AND 连接
   * <p>全匹配或部分匹配请使用 {@link #like(String, String)} 或 {@link #endWith(String, String)}
   *
   * @param column
   * @param value
   * @return
   */
  public T startWith(final String column, final String value) {
    return startWith(column, value, Logic.AND);
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>startWith("name", "abc", "OR");生成的sql为：</code>
   * <p>[OR] name like 'abc%';
   *
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   * <p>全匹配或部分匹配请使用 {@link #like(String, String)} 或 {@link #endWith(String, String)}
   *
   * @param column
   * @param value
   * @param logic
   * @return
   */
  public T startWith(final String column, final String value, final Logic logic) {
    if (StringUtils.isBlank(value)) {
      appendOrTrue(logic);
      return that;
    }
    appendWhere(logic, column + " like ?");
    whereParams.add(value + "%");
    return that;
  }

  /**
   * 任意列LIKE value%：
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 someStartWith(["col1", "col2", "a", Logic.AND]) 则转化为 AND ( col1 LIKE 'a%' OR col2 LIKE 'a%')</p>
   * <p>- 如 someStartWith(["col1", "col2", "a", Logic.OR]) 则转化为 OR ( col1 LIKE 'a%' OR col2 LIKE 'a%')</p>
   * @param columns 多个列
   * @param value 关键字
   * @param logic 外围逻辑
   * @return
   */
  public T someStartWith(final String[] columns, final String value, final Logic logic) {
    if (StringUtils.isBlank(value)) {
      appendOrTrue(logic);
      return that;
    }

    appendNColumnExpr(logic, columns, Oper.LIKE, value + "%", Logic.OR);

    return that;
  }

  /**
   * AND 任意列LIKE value%:
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>如 someStartWith(["col1", "col2", "a") 则转化为 AND ( col1 LIKE 'a%' OR col2 LIKE 'a%')</p>
   * @param columns 多个列
   * @param value 关键字
   * @return
   */
  public T someStartWith(final String[] columns, final String value) {
    return someStartWith(columns, value, Logic.AND);
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>endWith("name", "abc"); //生成的sql为：</code>
   * <p>[AND] name like '%abc';
   *
   * <p>默认使用 AND 连接
   * <p>全匹配或部分匹配请使用 {@link #like(String, String)} 或 {@link #startWith(String, String)}
   *
   * @param column
   * @param value
   * @return
   */
  public T endWith(final String column, final String value) {
    return endWith(column, value, Logic.AND);
  }

  /**
   * where 表达式中的 LIKE 语句，其参数为数组，按照如下处理逻辑：
   * <p>- <code>如果value为blank，则会忽略此条件</code>
   * <p>- <code>endWith("name", "abc", "OR"); //生成的sql为：</code>
   * <p>[OR] name like '%abc';
   *
   * <p>使用 logic 逻辑符 连接，如果前面没有任何条件表达式，则 logic 会被忽略
   * <p>全匹配或部分匹配请使用 {@link #like(String, String)} 或 {@link #startWith(String, String)}
   *
   * @param column
   * @param value
   * @param logic
   * @return
   */
  public T endWith(final String column, final String value, final Logic logic) {
    if (StringUtils.isBlank(value)) {
      appendOrTrue(logic);
      return that;
    }
    appendWhere(logic, column + " like ?");
    whereParams.add("%" + value);
    return that;
  }

  /**
   * 任意列LIKE %value：
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>- 如 someEndWith(["col1", "col2", "a", Logic.AND]) 则转化为 AND ( col1 LIKE '%a' OR col2 LIKE '%a')</p>
   * <p>- 如 someEndWith(["col1", "col2", "a", Logic.OR]) 则转化为 OR ( col1 LIKE '%a' OR col2 LIKE '%a')</p>
   * @param columns 多个列
   * @param value 关键字
   * @param logic 外围逻辑
   * @return
   */
  public T someEndWith(final String[] columns, final String value, final Logic logic) {
    if (StringUtils.isBlank(value)) {
      appendOrTrue(logic);
      return that;
    }

    appendNColumnExpr(logic, columns, Oper.LIKE, "%" + value, Logic.OR);

    return that;
  }

  /**
   * AND 任意列LIKE %value:
   * <p>- <code>如果value为blank，则会忽略此条件</code></p>
   * <p>如 someEndWith(["col1", "col2", "a") 则转化为 AND ( col1 LIKE '%a' OR col2 LIKE '%a')</p>
   * @param columns 多个列
   * @param value 关键字
   * @return
   */
  public T someEndWith(final String[] columns, final String value) {
    return someEndWith(columns, value, Logic.AND);
  }

  /**
   * 将条件语句append到where语句中，如果之前没有语句，则忽略逻辑操作符
   *
   * @param logicOp AND OR
   * @param expr    表达式
   */
  private void appendWhere(final Logic logicOp, final String expr) {
    if (whereColumns.isEmpty()) {
      whereColumns.add(expr);
    } else {
      whereColumns.add(String.format("%s (%s)", logicOp.getLogic(), expr));
    }
  }

  /**
   * 如果是OR逻辑表达式，则自动添加 OR 1=1，如果是AND，则什么都不添加
   * @param logicOp
   */
  private void appendOrTrue(final Logic logicOp) {
    if (logicOp == Logic.OR) {
      appendWhere(Logic.OR, "1=1");
    }
  }

  /**
   * 添加N列表达式
   * @param logicOp 外部逻辑运算符
   * @param columns 多个列
   * @param op 操作符
   * @param value 值
   * @param logic 内部逻辑运算符
   * @return
   */

  private void appendNColumnExpr(final Logic logicOp, final String[] columns, final Oper op, final Object value,
      final Logic innerLogic) {
    var joiner = Collectors.joining(StringUtils.wrap(innerLogic.getLogic(), ' '), "(", ")");
    var expr = Arrays.stream(columns).map(op::makeExpr).collect(joiner);
    appendWhere(logicOp, expr);
    if (op.hasValue()) {
      for (int i = 0; i < columns.length; i++) {
        whereParams.add(value);
      }
    }
  }

  /**
   * 生成，有N个参数的表达式，主要用于IN语句等
   *
   * @param column
   * @param op
   * @param nCount
   * @return
   */
  private String makeINExpr(final String column, final String op, final int nCount) {
    final StringBuilder expr = new StringBuilder();
    int idx = 0;
    expr.append(column).append(" ").append(op).append(" (");

    while (idx < nCount) {
      if (idx++ != 0) {
        expr.append(", ");
      }
      expr.append('?');
    }
    expr.append(')');

    return expr.toString();
  }

}
