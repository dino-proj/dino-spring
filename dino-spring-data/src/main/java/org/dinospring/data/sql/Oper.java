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

package org.dinospring.data.sql;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 19:13:14
 */

public enum Oper {
  EQ("=", "%s = ?"), GT(">", "%s > ?"), LT("<", "%s < ?"), NE("<>", "%s <> ?"), GTE(">=", "%s >= ?"),
  LTE("<=", "%s <= ?"), LIKE("LIKE", "%s LIKE ?"), NOT_LIKE("NOT LIKE", "%s NOT LIKE ?"), IN("IN", "%s IN (%s)"),
  NOT_IN("NOT IN", "%s NOT IN (%s)"), IS_NULL("IS NULL", "%s IS NULL"), IS_NOT_NULL("IS NOT NULL", "%s IS NOT NULL"),
  BETWEEN("BETWEEN", "%s BETWEEN ? AND ?"), EXISTS("EXISTS", "EXISTS (%s)");

  private final String op;
  private final String expr;
  private final int paramCount;
  private final int valueCount;

  /**
   *
   * @param op the oprator
   */
  private Oper(String op, String expr) {
    this.op = op;
    this.expr = expr;
    this.paramCount = StringUtils.countMatches(expr, '%');
    this.valueCount = StringUtils.countMatches(expr, '?');
  }

  /**
   * 返回操作符
   * @return
   */
  public String getOp() {
    return op;
  }

  /**
   * 生成相应的表达式
   * @param params
   * @return
   */
  public String makeExpr(final String... params) {
    if (params.length != this.paramCount) {
      throw new IllegalArgumentException(op + " need " + paramCount + " param(s), actule is " + params.length);
    }
    return String.format(expr, (Object[]) params);
  }

  public boolean hasValue() {
    return valueCount > 0;
  }

  @Override
  public String toString() {
    return this.op;
  }
}
