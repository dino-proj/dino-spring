// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller.support;

import java.util.List;
import java.util.stream.Collectors;

import org.dinospring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchFieldStatusCreateTimePeriodQuery<M extends FieldEnum> extends CreateTimePeriodStatusQuery {

  @Schema(name = "search", description = "数据库字段搜索")
  private SearchFieldMeta<M> search;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (search != null) {
      List<String> fields = search.getField().stream().map(M::getField).collect(Collectors.toList());
      sql.someLike(fields.toArray(new String[fields.size()]), search.getKeyword());
    }
    return super.buildSql(sql);
  }
}
