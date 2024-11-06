// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

/**
 *
 * @author Cody Lu
 */

public interface TaskObserver {
  /**
   * 获取任务ID
   * @return
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
