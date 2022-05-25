package org.dinospring.commons.utils;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.ThreadUtils;
import org.dinospring.commons.promise.Defer;
import org.dinospring.commons.promise.Promise;

import lombok.experimental.UtilityClass;

/**
 *
 * @author tuuboo
 * @date 2022-03-11 16:11:31
 */

@UtilityClass
public class TaskUtils {

  /**
   * 无参数，无返回的
   * @param task 任务
   * @param attempts 重试次数
   */
  public static void exec(Runnable task, int attempts) {
    exec(task, attempts, null);
  }

  /**
   * 无参数，无返回的
   * @param task 任务
   * @param attempts 重试次数
   * @param nap 重试间隔时长
   */
  public static void exec(Runnable task, int attempts, Duration nap) {
    while (attempts-- > 0) {
      try {
        task.run();
        return;
      } catch (Throwable t) {
        // nap and retry again
        nap(nap);
      }
    }
  }

  /**
   * task接受一个参数，无返回值
   * @param <T> 参数类型
   * @param task 任务
   * @param param 传给任务的参数
   * @param attempts 重试次数
   */
  public static <T> void exec(Consumer<T> task, final T param, int attempts) {
    exec(task, param, attempts, null);
  }

  /**
   * task接受一个参数，无返回值
   * @param <T> 参数类型
   * @param task 任务
   * @param param 传给任务的参数
   * @param attempts 重试次数
   * @param nap 重试间隔时长
   */
  public static <T> void exec(Consumer<T> task, final T param, int attempts, Duration nap) {
    exec(() -> task.accept(param), attempts, nap);
  }

  /**
   * task接受两个参数，无返回值
   * @param <T> 第一个参数类型
   * @param <U> 第二个参数类型
   * @param task 任务
   * @param paramFirst 第一个参数
   * @param paramSecond 第二个参数
   * @param attempts 重试次数
   */
  public <T, U> void exec(BiConsumer<T, U> task, final T paramFirst, final U paramSecond, int attempts) {
    exec(task, paramFirst, paramSecond, attempts, null);
  }

  /**
   * task接受两个参数，无返回值
   * @param <T> 第一个参数类型
   * @param <U> 第二个参数类型
   * @param task 任务
   * @param paramFirst 第一个参数
   * @param paramSecond 第二个参数
   * @param attempts 重试次数
   * @param nap 重试间隔时长
   */
  public <T, U> void exec(BiConsumer<T, U> task, final T paramFirst, final U paramSecond, int attempts, Duration nap) {
    exec(() -> task.accept(paramFirst, paramSecond), attempts, nap);
  }

  /**
   * 无参数，有返回的任务调用
   * @param <R> 返回值类型
   * @param task 任务
   * @param checker 检查task返回值是否符合要求，不符合则重试
   * @param attempts 重试次数
   * @param throwOutExceptions 直接抛出的异常的类型
   */
  @SafeVarargs
  public static <R> Promise<R> exec(Callable<R> task, Predicate<R> checker, int attempts,
      Class<? extends RuntimeException>... throwOutExceptions) {
    return exec(task, checker, attempts, null, throwOutExceptions);
  }

  /**
   * 无参数，有返回的任务调用
   * @param <R> 返回值类型
   * @param task 任务
   * @param checker 检查task返回值是否符合要求，不符合则重试
   * @param attempts 重试次数, 1表示最多执行一次
   * @param nap 重试间隔时长
   * @param throwOutExceptions 直接抛出的异常的类型
   */
  @SafeVarargs
  public static <R> Promise<R> exec(Callable<R> task, Predicate<R> checker, int attempts,
      Duration nap, Class<? extends RuntimeException>... throwOutExceptions) {
    while (attempts-- > 0) {
      try {
        var ret = task.call();
        if (checker.test(ret)) {
          return Defer.resolve(ret);
        }
      } catch (Exception t) {
        // throw out the exception
        if (!Objects.isNull(throwOutExceptions) && TypeUtils.isInstanceOfAny(t, throwOutExceptions)) {
          throw TypeUtils.<RuntimeException>cast(t);
        }
        // nap and retry again
        if (attempts == 0) {
          return Defer.fail(t);
        }
      }
      nap(nap);
    }
    return Defer.fail();
  }

  /**
   * task接受一个参数，并返回执行结果
   * @param <T> 参数类型
   * @param <R> 返回值类型
   * @param task 任务
   * @param param 传给异步任务的参数
   * @param checker 检查task返回值是否符合要求，不符合则重试
   * @param attempts 重试次数, 1表示最多执行一次
   * @param throwOutExceptions 直接抛出的异常的类型
   * @return
   */
  @SafeVarargs
  public <T, R> Promise<R> exec(Function<T, R> task, final T param, Predicate<R> checker, int attempts,
      Class<? extends RuntimeException>... throwOutExceptions) {
    return exec(task, param, checker, attempts, null, throwOutExceptions);
  }

  /**
   * task接受一个参数，并返回执行结果
   * @param <T> 参数类型
   * @param <R> 返回值类型
   * @param task 任务
   * @param param 传给异步任务的参数
   * @param checker 检查task返回值是否符合要求，不符合则重试
   * @param attempts 重试次数, 1表示最多执行一次
   * @param nap 重试间隔时长
   * @param throwOutExceptions 直接抛出的异常的类型
   * @return
   */
  @SafeVarargs
  public <T, R> Promise<R> exec(Function<T, R> task, final T param, Predicate<R> checker, int attempts,
      Duration nap, Class<? extends RuntimeException>... throwOutExceptions) {
    return exec(() -> task.apply(param), checker, attempts, nap, throwOutExceptions);
  }

  /**
   * task接受两个参数，并返回执行结果
   * @param <T> 第一个参数类型
   * @param <U> 第二个参数类型
   * @param <R> 返回结果类型
   * @param task 任务
   * @param paramFirst 第一个参数
   * @param paramSecond 第二个参数
   * @param checker 检查task返回值是否符合要求，不符合则重试
   * @param attempts 重试次数, 1表示最多执行一次
   * @param throwOutExceptions 直接抛出的异常的类型
   * @return
   */
  @SafeVarargs
  public <T, U, R> Promise<R> exec(BiFunction<T, U, R> task, final T paramFirst, final U paramSecond,
      Predicate<R> checker, int attempts, Class<? extends RuntimeException>... throwOutExceptions) {
    return exec(task, paramFirst, paramSecond, checker, attempts, null, throwOutExceptions);
  }

  /**
   * task接受两个参数，并返回执行结果
   * @param <T> 第一个参数类型
   * @param <U> 第二个参数类型
   * @param <R> 返回结果类型
   * @param task 任务
   * @param paramFirst 第一个参数
   * @param paramSecond 第二个参数
   * @param checker 检查task返回值是否符合要求，不符合则重试
   * @param attempts 重试次数, 1表示最多执行一次
   * @param nap 重试间隔时长
   * @param throwOutExceptions 直接抛出的异常的类型
   * @return
   */
  @SafeVarargs
  public <T, U, R> Promise<R> exec(BiFunction<T, U, R> task, final T paramFirst, final U paramSecond,
      Predicate<R> checker, int attempts, Duration nap,
      Class<? extends RuntimeException>... throwOutExceptions) {
    return exec(() -> task.apply(paramFirst, paramSecond), checker, attempts, nap, throwOutExceptions);
  }

  /**
   * 线程休息一会
   * @param nap 时间长度
   * @return true：如果没被打断，false：如果被打断。
   */
  public static boolean nap(Duration nap) {
    if (!Objects.isNull(nap)) {
      try {
        ThreadUtils.sleep(nap);
        return true;
      } catch (InterruptedException e) {
        return false;
      }
    } else {
      return true;
    }
  }

}
