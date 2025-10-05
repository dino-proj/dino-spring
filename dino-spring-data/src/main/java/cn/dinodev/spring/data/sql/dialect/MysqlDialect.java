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
  private final NamingConversition namingConversitionInstance;

  /**
   * 构造函数，创建MySQL数据库方言实例
   * @param metaData 数据库元数据
   * @param namingConversition 命名转换策略
   */
  @SuppressWarnings("unused")
  public MysqlDialect(DatabaseMetaData metaData, NamingConversition namingConversition) {
    this.namingConversitionInstance = namingConversition;
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
