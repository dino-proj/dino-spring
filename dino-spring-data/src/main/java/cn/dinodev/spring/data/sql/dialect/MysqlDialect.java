// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.sql.dialect;

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:14:39
 */

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
