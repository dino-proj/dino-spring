// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.sql.dialect;

import java.util.Map;
import java.util.WeakHashMap;

import org.dinospring.commons.utils.NamingUtils;

/**
 *
 * @author Cody LU
 * @date 2022-03-07 19:15:37
 */

public class SnakeNamingConversition implements NamingConversition {

  private static final Map<String, String> NAMING_CACHE = new WeakHashMap<>(1000);

  @Override
  public String convertColumnName(String colName) {
    var val = NAMING_CACHE.get(colName);
    if (val == null) {
      val = NamingUtils.toSnake(colName);
      NAMING_CACHE.put(colName, val);
    }
    return val;
  }

  @Override
  public String convertTableName(String tableName) {
    return convertColumnName(tableName);
  }

}
