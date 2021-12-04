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

package org.dinospring.core.modules.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantableEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author tuuboo
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_task")
public class TaskEntity extends TenantableEntityBase<String> {

  @Schema(description = "任务的名称", maxLength = 256)
  @Column(name = "task_name", length = 256, nullable = false)
  private String taskName;

  @Schema(description = "任务的输出消息", maxLength = 2048)
  @Column(name = "task_msg", length = 2048)
  private String taskMsg;

  @Schema(description = "任务的超时时间(毫秒)")
  @Column(name = "task_timeout", nullable = true)
  private Long taskTimeout;

  @Schema(description = "任务的进度", minimum = "0", maximum = "100")
  @Column(name = "task_progress", nullable = false)
  private Integer taskProgress;

}
