// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller.support;

import org.apache.commons.lang3.ArrayUtils;
import cn.dinodev.spring.core.service.CustomQuery;
import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
 * @author JL
 */

@Data
public class StatusQuery implements CustomQuery {
  @Schema(description = "状态，默认查询全部")
  private String[] status;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    return sql.inIf(ArrayUtils.isNotEmpty(status), "t.status", status);
  }

}
