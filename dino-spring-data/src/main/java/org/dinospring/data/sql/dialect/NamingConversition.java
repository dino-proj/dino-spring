// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.sql.dialect;

/**
 *
 * @author Cody Lu
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
