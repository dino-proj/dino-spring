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

package org.dinospring.core.controller.support;

import java.util.List;
import java.util.stream.Collectors;

import org.dinospring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
public class SearchFieldStatusCreateTimePeriodQuery<M extends FieldEnum> extends CreateTimePeriodStatusQuery {

  @Schema(name = "search", description = "数据库字段搜索")
  private SearchFieldMeta<M> search;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (search != null) {
      List<String> fields = search.getField().stream().map(file -> file.getField()).collect(Collectors.toList());
      sql.someLike(fields.toArray(new String[fields.size()]), search.getKeyword());
    }
    return super.buildSql(sql);
  }
}
