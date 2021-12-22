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

package com.botbrain.dino.sql.dialect;

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;

public class MysqlDialect implements Dialect {
  private final NamingConversition namingConversition;

  public MysqlDialect(DatabaseMetaData metaData, NamingConversition namingConversition) {
    this.namingConversition = namingConversition;
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
    return "SELECT uuid()";
  }

  @Override
  public String quoteTableName(String name) {
    return StringUtils.wrapIfMissing(name, '\"');
  }

  @Override
  public String getSequenceNextValSql(String sequenceName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean supportSequence() {
    return false;
  }

  @Override
  public boolean supportUUID() {
    return true;
  }
}
