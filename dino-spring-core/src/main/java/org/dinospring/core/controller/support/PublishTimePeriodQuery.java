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

import com.botbrain.dino.sql.builder.SelectSqlBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dinospring.commons.data.TimePeriod;
import org.dinospring.core.service.CustomQuery;

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
public class PublishTimePeriodQuery implements CustomQuery {

  @Schema(name = "publish_period", description = "发布时间范围查询")
  private TimePeriod publishPeriod;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    if (publishPeriod == null) {
      return sql;
    }
    return sql.between("publish_at", publishPeriod);
  }
}
