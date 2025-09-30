// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.iam;

import cn.dinodev.spring.core.controller.support.StatusQuery;
import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Cody Lu
 * @date 2022-05-04 22:44:08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleSearch extends StatusQuery {
  @Schema(description = "根据名字模糊查询")
  private String name;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    sql.like(RoleEntity.Fields.name, this.name);
    return super.buildSql(sql);
  }
}
