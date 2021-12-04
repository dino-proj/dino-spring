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

package org.dinospring.commons.utils;

/**
 *
 * @author tuuboo
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
    FAILD;
  }
}
