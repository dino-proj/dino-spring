// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.task;

import org.dinospring.core.vo.VoImplBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TaskVo extends VoImplBase<String> {

  @Schema(description = "任务的名称", maxLength = 256)
  private String taskName;

  @Schema(description = "任务的输出消息", maxLength = 2048)
  private String taskMsg;

  @Schema(description = "任务的步骤总数")
  @Column(name = "task_steps", nullable = true)
  private Integer taskSteps;

  @Schema(description = "任务的当前步骤")
  @Column(name = "task_current_step", nullable = true)
  private Integer taskCurrentStep;

  @Schema(description = "任务的进度", minimum = "0", maximum = "100")
  private Integer taskProgress;
}
