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

import java.util.List;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 19:13:51
 */

public interface SqlBuilder {
  /**
   * 获取生成的sql语句
   * @return
   */
  String getSql();

  /**
   * 获取sql语句需要的参数数组
   * @return
   */
  Object[] getParams();

  /**
    * Constructs a list of items with given separators.
    *
    * @param sql  StringBuilder to which the constructed string will be appended.
    * @param list List of objects (usually strings) to join.
    * @param start String to be added to the start of the list, before any of the
    * <p>            items.
    * @param sep  Separator string to be added between items in the list.
    * @param end  String to be append to the end of the list, after all of the
    * <p>            items.
    */
  default StringBuilder appendList(final StringBuilder sql, final List<?> list, final String start, final String sep,
      final String end) {
    var first = true;

    for (final Object s : list) {
      if (first) {
        sql.append(start);
      } else {
        sql.append(sep);
      }
      sql.append(s);
      first = false;
    }
    if (end != null && !list.isEmpty()) {
      sql.append(end);
    }
    return sql;
  }

  default StringBuilder appendList(final StringBuilder sql, final List<?> list, final String start,
      final String sep) {
    return appendList(sql, list, start, sep, null);
  }
}
