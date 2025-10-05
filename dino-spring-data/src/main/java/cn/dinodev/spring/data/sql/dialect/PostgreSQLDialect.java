// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.sql.dialect;

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:15:17
 */

public class PostgreSQLDialect implements Dialect {
  private final NamingConversition namingConversitionInstance;
  private final String uuidSql;

  /**
   * 构造函数，创建PostgreSQL数据库方言实例
   * @param metaData 数据库元数据
   * @param namingConversition 命名转换策略
   * @throws SQLException SQL异常
   */
  public PostgreSQLDialect(DatabaseMetaData metaData, NamingConversition namingConversition) throws SQLException {
    this.namingConversitionInstance = namingConversition;
    var majorVer = metaData.getDatabaseMajorVersion();
    if (majorVer >= 13) {
      uuidSql = "SELECT gen_random_uuid()";
    } else {
      uuidSql = "SELECT uuid_generate_v4()";
    }

  }

  @Override
  public NamingConversition namingConversition() {
    return namingConversitionInstance;
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
