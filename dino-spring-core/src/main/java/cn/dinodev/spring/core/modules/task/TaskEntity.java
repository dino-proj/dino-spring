// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import cn.dinodev.spring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_task")
public class TaskEntity extends TenantRowEntityBase<String> {

  @Schema(description = "任务的名称", maxLength = 256)
  @Column(name = "task_name", length = 256, nullable = false)
  private String taskName;

  @Schema(description = "任务的输出消息", maxLength = 2048)
  @Column(name = "task_msg", length = 2048)
  private String taskMsg;

  @Schema(description = "任务的超时时间(毫秒)")
  @Column(name = "task_timeout", nullable = true)
  private Long taskTimeout;

  @Schema(description = "任务的步骤总数")
  @Column(name = "task_steps", nullable = true)
  private Integer taskSteps;

  @Schema(description = "任务的当前步骤")
  @Column(name = "task_current_step", nullable = true)
  private Integer taskCurrentStep;

  @Schema(description = "任务的进度", minimum = "0", maximum = "100")
  @Column(name = "task_progress", nullable = false)
  private Integer taskProgress;

}
