package com.botbrain.dino.sql.dialect;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

public interface Dialect {
  /**
   * 生成limit offset语句
   */
  String limitOffset(int limit, long offset);

  /**
   * 生成 查询UUID的语句
   */
  String getSelectUUIDSql();

  String quoteTableName(String name);

  public class DEFAULT implements Dialect {

    @Override
    public String limitOffset(int limit, long offset) {
      if (limit > 0) {
        return offset > 0 ? "LIMIT " + limit + " OFFSET " + offset : "LIMIT " + limit;
      }
      return "";
    }

    @Override
    public String getSelectUUIDSql() {
      throw new NotImplementedException();
    }

    @Override
    public String quoteTableName(String name) {
      return StringUtils.wrapIfMissing(name, '\"');
    }
  }
}
