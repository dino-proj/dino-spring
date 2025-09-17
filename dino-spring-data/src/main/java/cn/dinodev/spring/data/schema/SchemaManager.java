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
  private JdbcOperations jdbcOperations;
  private List<TableDef> tables = new ArrayList<>();

  public SchemaManager(JdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  public void addTable(TableDef table) {
    tables.add(table);
  }
}
