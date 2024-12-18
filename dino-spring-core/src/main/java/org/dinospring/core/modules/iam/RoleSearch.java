/*
 *  Copyright 2021 dinodev.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dinospring.core.modules.iam;

import org.dinospring.core.controller.support.StatusQuery;
import org.dinospring.data.sql.builder.SelectSqlBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Cody Lu
 * @date 2022-05-04 22:44:08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleSearch extends StatusQuery {
  @Schema(description = "根据名字模糊查询")
  private String name;

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    sql.like(RoleEntity.Fields.name, this.name);
    return super.buildSql(sql);
  }
}
