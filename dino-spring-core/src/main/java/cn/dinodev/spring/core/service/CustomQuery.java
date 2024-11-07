// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.service;

import java.io.Serializable;

import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

/**
 *
 * @author Cody Lu
 */

public interface CustomQuery extends Serializable {
  /**
   * 构建查询语句
   * @param sql
   * @return
   */
  SelectSqlBuilder buildSql(SelectSqlBuilder sql);
}
