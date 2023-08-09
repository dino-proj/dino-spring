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

import org.dinospring.commons.data.TimePeriod;
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
public class CreateTimePeriodStatusQuery extends StatusQuery {

  @Schema(name = "create_period", description = "创建时间范围查询")
  private TimePeriod createPeriod;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (createPeriod != null) {
      sql.between("create_at", createPeriod);
    }
    return super.buildSql(sql);
  }
}
