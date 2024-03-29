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

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author Cody LU
 * @date 2022-03-07 19:15:17
 */

public class PostgreSQLDialect implements Dialect {
  private final NamingConversition namingConversition;
  private final String uuidSql;

  public PostgreSQLDialect(DatabaseMetaData metaData, NamingConversition namingConversition) throws SQLException {
    this.namingConversition = namingConversition;
    var majorVer = metaData.getDatabaseMajorVersion();
    if (majorVer >= 13) {
      uuidSql = "SELECT gen_random_uuid()";
    } else {
      uuidSql = "SELECT uuid_generate_v4()";
    }

  }

  @Override
  public NamingConversition namingConversition() {
    return namingConversition;
  }

  @Override
  public String limitOffset(int limit, long offset) {
    if (limit > 0) {
      return offset > 0 ? "LIMIT " + limit + " OFFSET " + offset : "LIMIT " + limit;
    }
    return "";
  }

  @Override
  public String getSelectUUIDSql() {
    return uuidSql;
  }

  @Override
  public String quoteTableName(String name) {
    return StringUtils.wrapIfMissing(name, '\"');
  }

  @Override
  public String getSequenceNextValSql(String sequenceName) {
    return "SELECT nextval('" + sequenceName + "')";
  }

  @Override
  public boolean supportSequence() {
    return true;
  }

  @Override
  public boolean supportUUID() {
    return true;
  }

}
