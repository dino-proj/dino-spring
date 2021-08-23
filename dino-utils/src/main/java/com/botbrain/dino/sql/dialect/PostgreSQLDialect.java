package com.botbrain.dino.sql.dialect;

import org.apache.commons.lang3.StringUtils;

public class PostgreSQLDialect implements Dialect {

  @Override
  public String limitOffset(int limit, long offset) {
    if (limit > 0) {
      return offset > 0 ? "LIMIT " + limit + " OFFSET " + offset : "LIMIT " + limit;
    }
    return "";
  }

  @Override
  public String getSelectUUIDSql() {
    return "SELECT gen_random_uuid()";
  }

  @Override
  public String quoteTableName(String name) {
    return StringUtils.wrapIfMissing(name, '\"');
  }
}
