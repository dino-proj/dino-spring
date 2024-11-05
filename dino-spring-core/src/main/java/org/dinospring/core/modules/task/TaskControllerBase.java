// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.task;

import jakarta.annotation.Nonnull;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 *
 * @author Cody LU
 */

public interface TaskControllerBase {

  /**
  * Service 服务实例
  * @return
  */
  @Nonnull
  default TaskService taskService() {
    return ContextHelper.findBean(TaskService.class);
  }

  /**
   * 查询任务信息，包括状态，进度，错误信息等
      * @param id
   * @return
   */
  @Operation(summary = "查询任务信息，包括状态，进度，错误信息等")
  @Parameter(name = "id", description = "任务的ID")
  @GetMapping("/info")
  default Response<TaskVo> getTaskInfo(@RequestParam String id) {
    return Response.success(taskService().getTaskInfo(id));
  }
}
