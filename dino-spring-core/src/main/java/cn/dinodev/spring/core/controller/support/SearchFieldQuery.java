// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller.support;

import java.util.List;
import java.util.stream.Collectors;

import cn.dinodev.spring.core.service.CustomQuery;
import cn.dinodev.spring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author JL
 * @Date: 2021/10/29
 */
@Data
public class SearchFieldQuery<M extends FieldEnum> implements CustomQuery {

  @Schema(name = "search", description = "数据库字段搜索")
  private SearchFieldMeta<M> search;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (search == null) {
      return sql;
    }
    List<String> fields = search.getField().stream().map(file -> file.getField()).collect(Collectors.toList());
    return sql.someLike(fields.toArray(new String[fields.size()]), search.getKeyword());
  }
}
