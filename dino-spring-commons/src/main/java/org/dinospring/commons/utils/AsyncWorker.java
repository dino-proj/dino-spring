// Copyright 2021 dinodev.cn
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

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 异步执行的任务
 * @author tuuboo
 */
@Component
@Async
@AllArgsConstructor
public class AsyncWorker {

  /**
   * 无参数，无返回的异步调用
   * @param task 异步任务
   */
  public void exec(Runnable task) {
    task.run();
  }

  /**
   * 无参数，无返回的异步调用
   * @param task 异步任务
   */
  public <R> Future<R> exec(Callable<R> task) {
    var futureTask = new FutureTask<>(task);
    futureTask.run();
    return futureTask;
  }

  /**
   * task接受一个参数，无返回值
   * @param <T> 参数类型
   * @param task 异步任务
   * @param param 传给异步任务的参数
   */
  public <T> void exec(Consumer<T> task, final T param) {
    task.accept(param);
  }

  /**
   * task接受一个参数，并返回执行结果
   * @param <T> 参数类型
   * @param <R> 返回值类型
   * @param task 异步任务
   * @param param 传给异步任务的参数
   * @return
   */
  public <T, R> Future<R> exec(Function<T, R> task, final T param) {
    var futureTask = new FutureTask<>(() -> task.apply(param));
    futureTask.run();
    return futureTask;
  }

  /**
   * task接受两个参数，无返回值
   * @param <T> 第一个参数类型
   * @param <U> 第二个参数类型
   * @param task 异步任务
   * @param paramFirst 第一个参数
   * @param paramSecond 第二个参数
   */
  public <T, U> void exec(BiConsumer<T, U> task, final T paramFirst, final U paramSecond) {
    task.accept(paramFirst, paramSecond);
  }

  /**
   * task接受两个参数，并返回执行结果
   * @param <T> 第一个参数类型
   * @param <U> 第二个参数类型
   * @param <R> 返回结果类型
   * @param task 异步任务
   * @param paramFirst 第一个参数
   * @param paramSecond 第二个参数
   * @return
   */
  public <T, U, R> Future<R> exec(BiFunction<T, U, R> task, final T paramFirst, final U paramSecond) {
    var futureTask = new FutureTask<>(() -> task.apply(paramFirst, paramSecond));
    futureTask.run();
    return futureTask;
  }

}
