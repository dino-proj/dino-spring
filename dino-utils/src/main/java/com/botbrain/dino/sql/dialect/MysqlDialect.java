package com.botbrain.dino.sql.dialect;

import org.apache.commons.lang3.StringUtils;

public class MysqlDialect implements Dialect {

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

}
