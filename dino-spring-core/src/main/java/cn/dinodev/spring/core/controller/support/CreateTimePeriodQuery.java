// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller.support;

import cn.dinodev.spring.commons.data.TimePeriod;
import cn.dinodev.spring.core.service.CustomQuery;
import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
public class CreateTimePeriodQuery implements CustomQuery {

  @Schema(name = "create_period", description = "创建时间范围查询")
  private TimePeriod createPeriod;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (createPeriod == null) {
      return sql;
    }
    return sql.between("create_at", createPeriod);
  }
}
