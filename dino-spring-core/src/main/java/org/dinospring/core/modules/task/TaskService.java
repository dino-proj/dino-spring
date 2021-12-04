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

import java.time.Duration;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dinospring.commons.utils.TaskObserver;

/**
 *
 * @author tuuboo
 */

public interface TaskService {

  /**
   * 创建任务
   * @param name 任务的名字
   * @param timeout 任务执行超时时间，null表示不超时
   * @param task 任务执行主体
   * @return
   */
  TaskVo runTask(@Nonnull String name, @Nullable Duration timeout, @Nonnull Function<TaskObserver, Boolean> task);

  /**
   * 获取任务信息
   * @param id
   * @return
   */
  TaskVo getTaskInfo(@Nonnull String id);

}
