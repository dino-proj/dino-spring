// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.utils;

/**
 * 任务观察者接口，用于监控任务执行状态
 *
 * @author Cody Lu
 */

public interface TaskObserver {
  /**
   * 获取任务ID
   *
   * @return 任务的唯一标识ID
   */
  String getId();

  /**
   * 设置任务消息
   * @param msg
   */
  void setMsg(String msg);

  /**
   * 更新任务进度
   * @param progress
   */
  void updateProgress(int progress);

  /**
   * 更新任务步数
   * @param step
   */
  void updateStep(int step);

  /**
   * 更新任务状态
   * @param status
   */
  void updateStatus(TaskStatus status);

  /**
   * 是否已经超时
   * @return
   */
  boolean isTimeout();

  /**
   * Enumeration representing the various states of a task execution lifecycle.
   * <p>
   * This enum defines the possible statuses that a task can have during its execution,
   * from initialization to completion (either successful or failed).
   * </p>
   */
  enum TaskStatus {
    //初始化
    INIT,
    //运行中
    RUNNING,
    //成功
    SUCCEED,
    //失败
    FAILED;
  }
}
