// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller.support;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.data.sql.builder.SelectSqlBuilder;

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchFieldStatusQuery<M extends FieldEnum> extends StatusQuery {

  @Schema(name = "search", description = "数据库字段搜索")
  private SearchFieldMeta<M> search;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (search != null && StringUtils.isNotBlank(search.getKeyword())) {
      sql.someLike(search.getField().stream().map(FieldEnum::getField).toArray(String[]::new), search.getKeyword());
    }
    return super.buildSql(sql);
  }
}
