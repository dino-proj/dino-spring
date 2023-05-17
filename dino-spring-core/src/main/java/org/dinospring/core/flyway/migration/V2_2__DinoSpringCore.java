// Copyright 2022 dinodev.cn
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

package org.dinospring.core.flyway.migration;

import java.util.Date;
import java.util.List;

import org.dinospring.core.entity.Code;
import org.dinospring.core.modules.iam.Action;
import org.dinospring.core.modules.iam.ActionGroupEntity;
import org.dinospring.core.modules.iam.ActionGroupRepository;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 * @date 2022-05-05 03:06:39
 */

@Component
@Slf4j
public class V2_2__DinoSpringCore extends BaseJavaMigration {

  @Autowired
  private ActionGroupRepository actionGroupRepository;

  @Override
  public void migrate(Context context) throws Exception {
    log.info("exec migration: V2_2__DinoSpringCore.migrate");
    actionGroupRepository.saveAll(actionGroups());
  }

  private List<ActionGroupEntity> actionGroups() {
    var iamActions = List.of(
        Action.builder().value("sys.iam:grant").label("管理").build());

    return List.of(
        ActionGroupEntity.builder().userType("admin").name("权限管理").remark("权限分配").actions(iamActions)
            .createAt(new Date()).updateAt(new Date()).status(Code.STATUS.OK.getName()).build());
  }

}
