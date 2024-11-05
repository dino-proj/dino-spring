// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.service;

import java.io.Serializable;

import org.dinospring.data.sql.builder.SelectSqlBuilder;

/**
 *
 * @author Cody LU
 */

public interface CustomQuery extends Serializable {
  /**
   * 构建查询语句
   * @param sql
   * @return
   */
  SelectSqlBuilder buildSql(SelectSqlBuilder sql);
}
