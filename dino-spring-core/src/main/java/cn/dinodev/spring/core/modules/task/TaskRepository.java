// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.core.modules.task;

import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cody Lu
 */
public interface TaskRepository extends CrudRepositoryBase<TaskEntity, String> {

  /**
   * 通过id修改任务的输出消息
   * @param id
   * @param msg
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE sys_task  SET task_msg=:msg WHERE id=:id")
  void updateTaskMsg(String id, String msg);

  /**
   * 通过id修改任务的进度
   * @param id
   * @param progress
   */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE sys_task  SET task_progress=:progress WHERE id=:id")
  void updateTaskProgress(String id, int progress);

  /**
  * 通过id修改任务的进度
  * @param id
  * @param step
  */
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  @Query("UPDATE sys_task SET task_current_step=:step WHERE id=:id")
  void updateTaskStep(String id, int step);
}
