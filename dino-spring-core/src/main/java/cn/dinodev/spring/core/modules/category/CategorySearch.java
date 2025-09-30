// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.category;

import cn.dinodev.spring.core.controller.support.StatusQuery;
import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JL
 * @Date: 2021/11/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategorySearch extends StatusQuery {
  @Schema(description = "根据名字模糊查询")
  private String name;

  @Schema(description = "根据父分类ID筛选")
  private Long parentId;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    sql.eqIfNotNull("parent_id", parentId);
    sql.like(CategoryEntityBase.Fields.name, this.name);
    return super.buildSql(sql);
  }
}
