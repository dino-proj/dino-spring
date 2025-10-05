// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.sql.dialect;

import java.util.Map;
import java.util.WeakHashMap;

import cn.dinodev.spring.commons.utils.NamingUtils;

/**
 * 蛇形命名转换器
 * 将Java驼峰命名转换为数据库蛇形命名格式，例如userName -> user_name
 *
 * @author Cody Lu
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
