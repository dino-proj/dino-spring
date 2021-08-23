package org.dinospring.commons.utils;

import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.botbrain.dino.utils.AsyncTask;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 * 异步执行的任务
 */
@Async
@Component
public class AsyncWorker {

  /**
   * 无参数，无返回的异步调用
   * @param task 异步任务
   */
  public void exec(AsyncTask task) {
    task.exec();
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
    return new AsyncResult<>(task.apply(param));
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
    return new AsyncResult<>(task.apply(paramFirst, paramSecond));
  }

}
