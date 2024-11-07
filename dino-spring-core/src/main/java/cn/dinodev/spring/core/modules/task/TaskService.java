// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.task;

import java.time.Duration;
import java.util.function.Predicate;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import cn.dinodev.spring.commons.utils.TaskObserver;

/**
 *
 * @author Cody Lu
 */

public interface TaskService {

  /**
   * 创建任务
   * @param name 任务的名字
   * @param timeout 任务执行超时时间，null表示不超时
   * @param task 任务执行主体
   * @return
   */
  TaskVo runTask(@Nonnull String name, @Nullable Duration timeout, @Nonnull Predicate<TaskObserver> task);

  /**
   * 创建带步数的任务
   * @param name 任务的名字
   * @param totalSteps 任务的总步数
   * @param timeout 任务执行超时时间，null表示不超时
   * @param task 任务执行主体
   * @return
   */
  TaskVo runStepTask(@Nonnull String name, int totalSteps, @Nullable Duration timeout,
      @Nonnull Predicate<TaskObserver> task);

  /**
   * 获取任务信息
   * @param id
   * @return
   */
  TaskVo getTaskInfo(@Nonnull String id);

}
