// Copyright 2021 dinodev.cn
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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.data.sql.builder.SelectSqlBuilder;

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
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
