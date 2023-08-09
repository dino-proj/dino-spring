// Copyright 2022 dinodev.cn
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

package org.dinospring.data.schema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cody LU
 * @date 2022-08-19 05:00:55
 */

@Component
public class SchemaManager {

  private JdbcOperations jdbcOperations;
  private List<TableDef> tables = new ArrayList<>();

  public SchemaManager(JdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  public void addTable(TableDef table) {
    tables.add(table);
  }
}
