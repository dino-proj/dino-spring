// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.task.impl;

import java.time.Duration;
import java.util.function.Predicate;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import cn.dinodev.spring.commons.utils.AsyncWorker;
import cn.dinodev.spring.commons.utils.TaskObserver;
import cn.dinodev.spring.core.entity.Code;
import cn.dinodev.spring.core.modules.task.TaskEntity;
import cn.dinodev.spring.core.modules.task.TaskRepository;
import cn.dinodev.spring.core.modules.task.TaskService;
import cn.dinodev.spring.core.modules.task.TaskVo;
import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import cn.dinodev.spring.data.domain.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Service
public class TaskServiceImpl extends ServiceBase<TaskEntity, String> implements TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private IdService idService;

  @Autowired
  private AsyncWorker asyncWorker;

  private final JdbcAggregateTemplate jdbcAggregateTemplate;

  public TaskServiceImpl(JdbcAggregateTemplate jdbcAggregateTemplate) {
    this.jdbcAggregateTemplate = jdbcAggregateTemplate;
  }

  @Override
  public CrudRepositoryBase<TaskEntity, String> repository() {
    return taskRepository;
  }

  @Override
  public TaskVo getTaskInfo(String id) {
    return projection(TaskVo.class, taskRepository.findById(id));
  }

  @Override
  public TaskVo runTask(@Nonnull String name, @Nullable Duration timeout,
      @Nonnull Predicate<TaskObserver> task) {
    var taskEntity = TaskEntity.builder()
        .id(idService.genUUID())
        .taskName(name)
        .taskProgress(0)
        .taskTimeout(timeout == null ? -1L : timeout.toMillis())
        .status(Code.TASK.INIT.getName())
        .build();

    this.beforeSaveEntity(taskEntity);
    taskEntity = this.jdbcAggregateTemplate.insert(taskEntity);
    startTask(taskEntity, timeout, task);

    return projection(TaskVo.class, taskEntity);

  }

  @Override
  public TaskVo runStepTask(String name, int totalSteps, Duration timeout, Predicate<TaskObserver> task) {

    var taskEntity = TaskEntity.builder()
        .id(idService.genUUID())
        .taskName(name)
        .taskSteps(totalSteps)
        .taskCurrentStep(1)
        .taskProgress(0)
        .taskTimeout(timeout == null ? -1L : timeout.toMillis())
        .status(Code.TASK.INIT.getName())
        .build();
    this.beforeSaveEntity(taskEntity);
    taskEntity = this.jdbcAggregateTemplate.insert(taskEntity);
    startTask(taskEntity, timeout, task);

    return projection(TaskVo.class, taskEntity);
  }

  private void startTask(TaskEntity taskEntity, Duration timeout, Predicate<TaskObserver> task) {
    var taskObserver = new TaskObserverImpl(taskEntity.getId(), timeout);

    asyncWorker.exec(() -> {
      taskObserver.updateStatus(TaskObserver.TaskStatus.RUNNING);
      try {
        boolean ret = task.test(taskObserver);
        if (ret) {
          taskObserver.updateStatus(TaskObserver.TaskStatus.SUCCEED);
        } else {
          taskObserver.updateStatus(TaskObserver.TaskStatus.FAILED);
        }
      } catch (RuntimeException e) {
        taskObserver.setMsg(e.getMessage());
        taskObserver.updateStatus(TaskObserver.TaskStatus.FAILED);
      }
    });
  }

  @Override
  public long updateStatusById(String id, String taskStatus) {
    TaskEntity entity = getById(id);
    entity.setStatus(taskStatus);
    TaskEntity updateById = updateById(entity);
    return updateById == null ? 0 : 1;
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
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(TaskStatus status) {
      switch (status) {
        case INIT:
          updateStatusById(taskId, Code.TASK.INIT.getName());
          break;

        case RUNNING:
          updateStatusById(taskId, Code.TASK.RUNNING.getName());
          break;

        case SUCCEED:
          updateStatusById(taskId, Code.TASK.SUCCEED.getName());
          taskRepository.updateTaskProgress(taskId, 100);
          break;

        case FAILED:
          updateStatusById(taskId, Code.TASK.FAILED.getName());
          taskRepository.updateTaskProgress(taskId, 100);
          break;

        default:
          break;

      }

    }

    @Override
    public void updateStep(int step) {
      taskRepository.updateTaskStep(taskId, step);
    }

  }

}
