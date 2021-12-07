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

package org.dinospring.core.modules.task.impl;

import lombok.RequiredArgsConstructor;
import org.dinospring.commons.utils.AsyncWorker;
import org.dinospring.commons.utils.TaskObserver;
import org.dinospring.core.entity.Code;
import org.dinospring.core.modules.task.TaskEntity;
import org.dinospring.core.modules.task.TaskRepository;
import org.dinospring.core.modules.task.TaskService;
import org.dinospring.core.modules.task.TaskVo;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CurdRepositoryBase;
import org.dinospring.data.domain.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.function.Function;

/**
 *
 * @author tuuboo
 */

@Service
public class TaskServiceImpl extends ServiceBase<TaskEntity, String> implements TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private IdService idService;

  @Autowired
  private AsyncWorker asyncWorker;

  @Override
  public CurdRepositoryBase<TaskEntity, String> repository() {
    return taskRepository;
  }

  @Override
  public TaskVo getTaskInfo(String id) {
    return projection(TaskVo.class, taskRepository.findById(id));
  }

  @Override
  public TaskVo runTask(@Nonnull String name, @Nullable Duration timeout,
                        @Nonnull Function<TaskObserver, Boolean> task) {
    var taskEntity = TaskEntity.builder()
      .id(idService.genUUID())
      .taskName(name)
      .taskProgress(0)
      .taskTimeout(timeout == null ? -1L : timeout.toMillis())
      .status(Code.TASK.INIT.getName())
      .build();
    taskEntity=this.save(taskEntity);

    var taskObserver = new TaskObserverImpl(taskEntity.getId(), timeout);

    asyncWorker.exec(() -> {
      taskObserver.updateStatus(TaskObserver.TaskStatus.RUNNING);
      try {
        boolean ret = task.apply(taskObserver);
        if (ret) {
          taskObserver.updateStatus(TaskObserver.TaskStatus.SUCCEED);
        } else {
          taskObserver.updateStatus(TaskObserver.TaskStatus.FAILD);
        }
      } catch (Throwable e) {
        taskObserver.setMsg(e.getMessage());
        taskObserver.updateStatus(TaskObserver.TaskStatus.FAILD);
      }
    });

    return projection(TaskVo.class, taskEntity);

  }

  @RequiredArgsConstructor
  private class TaskObserverImpl implements TaskObserver {
    @Nonnull
    private final String taskId;

    @Nonnull
    private final Duration timeout;

    private Long startAt = System.currentTimeMillis();

    @Override
    public String getId() {
      return taskId;
    }

    @Override
    public void setMsg(String msg) {
      taskRepository.updateTaskMsg(taskId, msg);
    }

    @Override
    public void updateProgress(int progress) {
      taskRepository.updateTaskProgress(taskId, progress);
    }

    @Override
    public boolean isTimeout() {
      return timeout != null && startAt + timeout.toMillis() < System.currentTimeMillis();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateStatus(TaskStatus status) {
      switch (status) {
        case INIT:
          taskRepository.updateStatusById(taskId, Code.TASK.INIT.getName());
          break;

        case RUNNING:
          taskRepository.updateStatusById(taskId, Code.TASK.RUNNING.getName());
          break;

        case SUCCEED:
          taskRepository.updateStatusById(taskId, Code.TASK.SUCCEED.getName());
          taskRepository.updateTaskProgress(taskId, 100);
          break;

        case FAILD:
          taskRepository.updateStatusById(taskId, Code.TASK.FAILD.getName());
          taskRepository.updateTaskProgress(taskId, 100);
          break;

        default:
          break;

      }

    }

  }

}
