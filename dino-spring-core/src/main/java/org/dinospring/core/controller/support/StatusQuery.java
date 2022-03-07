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

import org.apache.commons.lang3.ArrayUtils;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.data.domain.EntityBase;
import org.dinospring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 * @author JL
 */

@Data
public class StatusQuery implements CustomQuery {
  @Schema(description = "状态，默认查询全部")
  private String[] status;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    return sql.inIf(ArrayUtils.isNotEmpty(status), EntityBase.Fields.status, status);
  }

}
