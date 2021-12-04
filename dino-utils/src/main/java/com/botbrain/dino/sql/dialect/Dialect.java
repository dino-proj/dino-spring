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

public interface Dialect {

  /**
   * 获取名字转换器
   */
  NamingConversition namingConversition();

  /**
   * 生成limit offset语句
   */
  String limitOffset(int limit, long offset);

  /**
   * 是否支持UUID语句
   * @return
   */
  boolean supportUUID();

  /**
   * 生成 查询UUID的语句
   */
  String getSelectUUIDSql();

  /**
   * 生成查询sequence的语句
   * @param sequenceName
   * @return
   */
  public String getSequenceNextValSql(String sequenceName);

  /**
   * 是否支持sequence
   * @return
   */
  public boolean supportSequence();

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
