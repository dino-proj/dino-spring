// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.schema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cody Lu
 * @date 2022-08-19 05:00:55
 */

@Component
public class SchemaManager {

  @SuppressWarnings("unused")
  private final JdbcOperations jdbcOperations;
  private final List<TableDef> tables = new ArrayList<>();

  /**
   * 构造函数，初始化模式管理器
   * @param jdbcOperations JDBC操作实例
   */
  public SchemaManager(JdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  /**
   * 添加表定义
   * @param table 表定义
   */
  public void addTable(TableDef table) {
    tables.add(table);
  }
}
