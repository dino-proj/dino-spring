package org.dinospring.commons.promise;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 * @date 2022-03-14 21:17:35
 */

@Slf4j
public abstract class AbstractPromise<D> implements Promise<D> {

  protected volatile State state = State.PENDING;

  protected final List<Consumer<? super D>> doneCallbacks = new CopyOnWriteArrayList<>();
  protected final List<Consumer<Throwable>> failCallbacks = new CopyOnWriteArrayList<>();
  protected final List<AlwaysCallback<? super D>> alwaysCallbacks = new CopyOnWriteArrayList<>();

  protected D resolveResult;
  protected Throwable rejectResult;

  @Override
  public State state() {
    return state;
  }

  @Override
  public Promise<D> done(Consumer<? super D> callback) {
    synchronized (this) {
      if (isResolved()) {
        triggerDone(callback, resolveResult);
      } else {
        doneCallbacks.add(callback);
      }
    }
    return this;
  }

  @Override
  public Promise<D> fail(Consumer<Throwable> callback) {
    synchronized (this) {
      if (isRejected()) {
        triggerFail(callback, rejectResult);
      } else {
        failCallbacks.add(callback);
      }
    }
    return this;
  }

  @Override
  public Promise<D> always(AlwaysCallback<? super D> callback) {
    synchronized (this) {
      if (isPending()) {
        alwaysCallbacks.add(callback);
      } else {
        triggerAlways(callback, state, resolveResult, rejectResult);
      }
    }
    return this;
  }

  protected void triggerDone(D resolved) {
    for (Consumer<? super D> callback : doneCallbacks) {
      triggerDone(callback, resolved);
    }
    doneCallbacks.clear();
  }

  protected void triggerDone(Consumer<? super D> callback, D resolved) {
    try {
      callback.accept(resolved);
    } catch (Exception e) {
      handleException(CallbackType.DONE_CALLBACK, e);
    }
  }

  protected void triggerFail(Throwable rejected) {
    for (var callback : failCallbacks) {
      triggerFail(callback, rejected);
    }
    failCallbacks.clear();
  }

  protected void triggerFail(Consumer<Throwable> callback, Throwable rejected) {
    try {
      callback.accept(rejected);
    } catch (Exception e) {
      handleException(CallbackType.FAIL_CALLBACK, e);
    }
  }

  protected void triggerAlways(State state, D resolve, Throwable reject) {
    for (AlwaysCallback<? super D> callback : alwaysCallbacks) {
      triggerAlways(callback, state, resolve, reject);
    }
    alwaysCallbacks.clear();

    synchronized (this) {
      this.notifyAll();
    }
  }

  protected void triggerAlways(AlwaysCallback<? super D> callback, State state,
      D resolve, Throwable reject) {
    try {
      callback.onAlways(state, resolve, reject);
    } catch (Exception e) {
      handleException(CallbackType.ALWAYS_CALLBACK, e);
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
    return state == State.PENDING;
  }

  @Override
  public boolean isResolved() {
    return state == State.RESOLVED;
  }

  @Override
  public boolean isRejected() {
    return state == State.REJECTED;
  }

  public void waitSafely() throws InterruptedException {
    waitSafely(-1);
  }

  public void waitSafely(long timeout) throws InterruptedException {
    final long startTime = System.currentTimeMillis();
    synchronized (this) {
      while (this.isPending()) {
        try {
          if (timeout <= 0) {
            wait();
          } else {
            final long elapsed = (System.currentTimeMillis() - startTime);
            final long waitTime = timeout - elapsed;
            wait(waitTime);
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw e;
        }

        if (timeout > 0 && ((System.currentTimeMillis() - startTime) >= timeout)) {
          return;
        } else {
          continue; // keep looping
        }
      }
    }
  }

  @Override
  public D get() {
    try {
      waitSafely();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (isResolved()) {
      return resolveResult;
    } else if (isRejected()) {
      throw new RuntimeException(rejectResult);
    }
    return resolveResult;
  }

  @Override
  public D getOrElse(D defaultValue) {
    try {
      waitSafely();
    } catch (InterruptedException e) {
      // DO nothing
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
    } catch (InterruptedException e) {
      // DO nothing
    }
    if (isResolved()) {
      return resolveResult;
    } else {
      return valueSupplier.get();
    }
  }

  protected void handleException(CallbackType callbackType, Exception e) {
    log.error("An uncaught exception occurred  in {}", callbackType, e);
  }

  protected enum CallbackType {
    DONE_CALLBACK,
    FAIL_CALLBACK,
    PROGRESS_CALLBACK,
    ALWAYS_CALLBACK
  }
}