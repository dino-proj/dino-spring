package cn.dinodev.spring.commons.promise;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

/**
 * Promise的抽象实现类，提供Promise模式的基础功能
 *
 * @param <D> Promise解决时的数据类型
 * @author Cody Lu
 * @since 2022-03-14
 */

@Slf4j
public abstract class AbstractPromise<D> implements Promise<D> {

  protected State promiseState = State.PENDING;
  protected final ReentrantLock lock = new ReentrantLock();
  protected final Condition condition = lock.newCondition();

  protected final List<Consumer<? super D>> doneCallbacks = new CopyOnWriteArrayList<>();
  protected final List<Consumer<Throwable>> failCallbacks = new CopyOnWriteArrayList<>();
  protected final List<AlwaysCallback<? super D>> alwaysCallbacks = new CopyOnWriteArrayList<>();

  protected D resolveResult;
  protected Throwable rejectResult;

  @Override
  public State state() {
    return promiseState;
  }

  @Override
  public Promise<D> done(Consumer<? super D> callback) {
    lock.lock();
    try {
      if (isResolved()) {
        triggerDone(callback, resolveResult);
      } else {
        doneCallbacks.add(callback);
      }
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public Promise<D> fail(Consumer<Throwable> callback) {
    lock.lock();
    try {
      if (isRejected()) {
        triggerFail(callback, rejectResult);
      } else {
        failCallbacks.add(callback);
      }
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public Promise<D> always(AlwaysCallback<? super D> callback) {
    lock.lock();
    try {
      if (isPending()) {
        alwaysCallbacks.add(callback);
      } else {
        triggerAlways(callback, promiseState, resolveResult, rejectResult);
      }
    } finally {
      lock.unlock();
    }
    return this;
  }

  /**
   * 触发已完成回调方法
   * @param resolved 解决的数据
   */
  protected void triggerDone(D resolved) {
    for (Consumer<? super D> callback : doneCallbacks) {
      triggerDone(callback, resolved);
    }
    doneCallbacks.clear();
  }

  /**
   * 触发单个已完成回调方法
   * @param callback 回调函数
   * @param resolved 解决的数据
   */
  protected void triggerDone(Consumer<? super D> callback, D resolved) {
    try {
      callback.accept(resolved);
    } catch (RuntimeException exception) {
      handleException(CallbackType.DONE_CALLBACK, exception);
    }
  }

  /**
   * 触发失败回调方法
   * @param rejected 拒绝的异常
   */
  protected void triggerFail(Throwable rejected) {
    for (var callback : failCallbacks) {
      triggerFail(callback, rejected);
    }
    failCallbacks.clear();
  }

  /**
   * 触发单个失败回调方法
   * @param callback 回调函数
   * @param rejected 拒绝的异常
   */
  protected void triggerFail(Consumer<Throwable> callback, Throwable rejected) {
    try {
      callback.accept(rejected);
    } catch (RuntimeException exception) {
      handleException(CallbackType.FAIL_CALLBACK, exception);
    }
  }

  /**
   * 触发总是执行的回调方法
   * @param state Promise状态
   * @param resolve 解决的数据
   * @param reject 拒绝的异常
   */
  protected void triggerAlways(State state, D resolve, Throwable reject) {
    for (AlwaysCallback<? super D> callback : alwaysCallbacks) {
      triggerAlways(callback, state, resolve, reject);
    }
    alwaysCallbacks.clear();

    lock.lock();
    try {
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  /**
   * 触发单个总是执行的回调方法
   * @param callback 回调函数
   * @param state Promise状态
   * @param resolve 解决的数据
   * @param reject 拒绝的异常
   */
  protected void triggerAlways(AlwaysCallback<? super D> callback, State state,
      D resolve, Throwable reject) {
    try {
      callback.onAlways(state, resolve, reject);
    } catch (RuntimeException exception) {
      handleException(CallbackType.ALWAYS_CALLBACK, exception);
    }
  }

  @Override
  public Promise<D> then(Consumer<? super D> callback) {
    return done(callback);
  }

  @Override
  public Promise<D> then(Consumer<? super D> doneCallback, Consumer<Throwable> failCallback) {
    done(doneCallback);
    fail(failCallback);
    return this;
  }

  @Override
  public boolean isPending() {
    return promiseState == State.PENDING;
  }

  @Override
  public boolean isResolved() {
    return promiseState == State.RESOLVED;
  }

  @Override
  public boolean isRejected() {
    return promiseState == State.REJECTED;
  }

  @Override
  public void waitSafely() throws InterruptedException {
    waitSafely(-1);
  }

  @Override
  public void waitSafely(long timeout) throws InterruptedException {
    final long startTime = System.currentTimeMillis();
    lock.lock();
    try {
      while (this.isPending()) {
        try {
          if (timeout <= 0) {
            condition.await();
          } else {
            final long elapsed = System.currentTimeMillis() - startTime;
            final long waitTime = timeout - elapsed;
            condition.await(waitTime, TimeUnit.MILLISECONDS);
          }
        } catch (InterruptedException e) {
          // Thread interruption is necessary for proper concurrent control in Promise implementation
          Thread.currentThread().interrupt();
          throw e;
        }

        if (timeout > 0 && (System.currentTimeMillis() - startTime) >= timeout) {
          return;
        } else {
          continue; // keep looping
        }
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public D get() {
    try {
      waitSafely();
    } catch (InterruptedException interruptedException) {
      log.error("Thread was interrupted while waiting for promise completion", interruptedException);
    }
    if (isResolved()) {
      return resolveResult;
    } else if (isRejected()) {
      throw new IllegalStateException("Promise was rejected", rejectResult);
    }
    return resolveResult;
  }

  @Override
  public D getOrElse(D defaultValue) {
    try {
      waitSafely();
    } catch (InterruptedException interruptedException) {
      log.debug("Thread was interrupted while waiting for promise completion, returning default value",
          interruptedException);
    }
    if (isResolved()) {
      return resolveResult;
    } else {
      return defaultValue;
    }
  }

  @Override
  public D getOrElse(Supplier<D> valueSupplier) {
    try {
      waitSafely();
    } catch (InterruptedException interruptedException) {
      log.debug("Thread was interrupted while waiting for promise completion, using value supplier",
          interruptedException);
    }
    if (isResolved()) {
      return resolveResult;
    } else {
      return valueSupplier.get();
    }
  }

  /**
   * 处理回调函数执行时的异常
   * @param callbackType 回调类型
   * @param exception 发生的异常
   */
  protected void handleException(CallbackType callbackType, Exception exception) {
    log.error("An uncaught exception occurred  in {}", callbackType, exception);
  }

  /**
   * Promise回调函数的类型
   */
  protected enum CallbackType {
    /**
     * 完成
     */
    DONE_CALLBACK,
    /**
     * 失败
     */
    FAIL_CALLBACK,
    /**
     * 进度
     */
    PROGRESS_CALLBACK,
    /**
     * 每次
     */
    ALWAYS_CALLBACK
  }
}