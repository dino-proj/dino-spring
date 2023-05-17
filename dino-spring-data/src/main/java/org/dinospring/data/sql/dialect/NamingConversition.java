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

/**
 *
 * @author tuuboo
 * @date 2022-03-07 19:14:59
 */

public interface NamingConversition {

  /**
   * 转换列的名字
   * @param colName
   * @return
   */
  String convertColumnName(String colName);

  /**
   * 转换表的名字
   * @param tableName
   * @return
   */
  String convertTableName(String tableName);

  public class Default implements NamingConversition {
    public static final Default INST = new Default();

    @Override
    public String convertColumnName(String colName) {
      return colName;
    }

    @Override
    public String convertTableName(String tableName) {
      return tableName;
    }

  }
}
