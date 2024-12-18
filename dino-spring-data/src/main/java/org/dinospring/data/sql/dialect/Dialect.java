// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.sql.dialect;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:14:24
 */

public interface Dialect {

  /**
   * 获取名字转换器
   * @return
   */
  NamingConversition namingConversition();

  /**
   * 生成limit offset语句
   * @param limit
   * @param offset
   * @return
   */
  String limitOffset(int limit, long offset);

  /**
   * 是否支持UUID语句
   * @return
   */
  boolean supportUUID();

  /**
   * 生成 查询UUID的语句
   * @return
   */
  String getSelectUUIDSql();

  /**
   * 生成查询sequence的语句
   * @param sequenceName
   * @return
   */
  String getSequenceNextValSql(String sequenceName);

  /**
   * 是否支持sequence
   * @return
   */
  boolean supportSequence();

  /**
   * 表描述
   * @param name
   * @return
   */
  String quoteTableName(String name);

  /**
   * 返回缺省的Dialect
   * @return
   */
  static Dialect ofDefault() {
    return Default.INST_DEFAULT;
  }

  public class Default implements Dialect {
    private static final Default INST_DEFAULT = new Default();

    @Override
    public String limitOffset(int limit, long offset) {
      if (limit > 0) {
        return offset > 0 ? "LIMIT " + limit + " OFFSET " + offset : "LIMIT " + limit;
      }
      return "";
    }

    @Override
    public String getSelectUUIDSql() {
      throw new UnsupportedOperationException();
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
    public String quoteTableName(String name) {
      return StringUtils.wrapIfMissing(name, '\"');
    }

    @Override
    public NamingConversition namingConversition() {
      return NamingConversition.Default.INST;
    }

    @Override
    public boolean supportUUID() {
      return false;
    }

  }
}
