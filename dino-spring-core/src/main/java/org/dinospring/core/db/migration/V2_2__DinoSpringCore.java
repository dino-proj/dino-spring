// Copyright 2022 dinospring.cn
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

package org.dinospring.core.db.migration;

import java.util.List;

import org.dinospring.core.modules.iam.Action;
import org.dinospring.core.modules.iam.ActionGroupEntity;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author tuuboo
 * @date 2022-05-05 03:06:39
 */

@Component
public class V2_2__DinoSpringCore extends BaseJavaMigration {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void migrate(Context context) throws Exception {
    //actionGroupRepository.saveAllAndFlush(actionGroups());
  }

  private List<ActionGroupEntity> actionGroups() {
    var iamActions = List.of(
        Action.builder().value("sys.iam:grant").label("管理").build(),
        Action.builder().value("sys.iam.role:create").label("角色创建").build(),
        Action.builder().value("sys.iam.role:view").label("角色查看").build());

    return List.of(
        ActionGroupEntity.builder().userType("admin").name("权限管理").remark("权限分配，角色管理").actions(iamActions).build());
  }

}
