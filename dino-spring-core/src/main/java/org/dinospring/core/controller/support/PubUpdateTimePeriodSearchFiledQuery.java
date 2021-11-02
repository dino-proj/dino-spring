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

/**
 * @author JL
 * @Date: 2021/11/1
 */
@Data
public class PubUpdateTimePeriodSearchFiledQuery extends SearchFiledQuery {

  @Schema(name = "publish_proid", description = "发布时间范围")
  private TimePeriod publishProid;

  @Schema(name = "update_proid", description = "更新时间范围")
  private TimePeriod updateProid;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    super.buildSql(sql);
    if (publishProid != null) {
      sql.between("publish_at", publishProid);
    }
    if (updateProid != null) {
      sql.between("update_at", updateProid);
    }
    return sql;
  }
}
