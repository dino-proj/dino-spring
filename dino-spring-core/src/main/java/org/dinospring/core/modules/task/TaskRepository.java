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
package org.dinospring.core.modules.task;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cody LU
 */
public interface TaskRepository extends CrudRepositoryBase<TaskEntity, String> {

  /**
   * 通过id修改任务的输出消息
   * @param id
   * @param msg
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE TaskEntity e SET e.taskMsg=:msg WHERE e.id=:id")
  void updateTaskMsg(String id, String msg);

  /**
   * 通过id修改任务的进度
   * @param id
   * @param progress
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE TaskEntity e SET e.taskProgress=:progress WHERE e.id=:id")
  void updateTaskProgress(String id, int progress);
}
