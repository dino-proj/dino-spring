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
