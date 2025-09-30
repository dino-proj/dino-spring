package cn.dinodev.spring.commons.promise;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Promise接口，用于观察对应Deferred对象上发生的操作
 * 
 * <p>Promise对象应该从{@link Deferred#promise()}获取，或通过DeferredManager使用。
 * 
 * <pre>
 * {@code
 * Deferred<String> deferredObject = new DeferredObject<>();
 * Promise<String> promise = deferredObject.promise();
 * promise.done(result -> {
 *     // 处理成功结果
 * });
 * 
 * // 另一个线程使用同一个deferredObject
 * deferredObject.resolve("OK");
 * }
 * </pre>
 *
 * @param <D> 用于成功回调的数据类型
 * @author Ray Tsang
 * @author Stephan Classen
 */
public interface Promise<D> extends Supplier<D> {
  enum State {
    /**
     * The Promise is still pending - it could be created, submitted for execution,
     * or currently running, but not yet finished.
     */
    PENDING,

    /**
     * The Promise has finished running and a failure occurred.
     * Thus, the Promise is rejected.
     *
     * @see Deferred#reject(Object)
     */
    REJECTED,

    /**
     * The Promise has finished running successfully.
     * Thus, the Promise is resolved.
     *
     * @see Deferred#resolve(Object)
     */
    RESOLVED
  }

  /**
   * 状态
   * @return the state of this promise.
   */
  State state();

  /**
   * Queries the state of this promise, returning {@code true} iff it is {@code State.PENDING}.
   *
   * @see State#PENDING
   * @return {@code true} if the current state of this promise is {@code State.PENDING}, {@code false} otherwise.
   */
  boolean isPending();

  /**
   * Queries the state of this promise, returning {@code true} iff it is {@code State.RESOLVED}.
   *
   * @see State#RESOLVED
   * @return {@code true} if the current state of this promise is {@code State.RESOLVED}, {@code false} otherwise.
   */
  boolean isResolved();

  /**
   * Queries the state of this promise, returning {@code true} iff it is {@code State.REJECTED}.
   *
   * @see State#REJECTED
   * @return {@code true} if the current state of this promise is {@code State.REJECTED}, {@code false} otherwise.
   */
  boolean isRejected();

  /**
   * get the resolved value， return defaultValue for rejected
   * @param defaultValue 默认值
   * @return 如果Promise已解决则返回解决的值，否则返回默认值
   */
  D getOrElse(D defaultValue);

  /**
   * get the resolved value， return defaultSupplier‘s result for rejected
   * @param defaultSupplier 默认值提供者
   * @return 如果Promise已解决则返回解决的值，否则返回默认值
   */
  D getOrElse(Supplier<D> defaultSupplier);

  /**
   * Equivalent to {@link #done(DoneCallback)}
   *
   * @param doneCallback see {@link #done(DoneCallback)}
   * @return {@code this} for chaining more calls
   */
  Promise<D> then(Consumer<? super D> doneCallback);

  /**
   * Equivalent to {@link #done(DoneCallback)}.{@link #fail(FailCallback)}
   *
   * @param doneCallback see {@link #done(DoneCallback)}
   * @param failCallback see {@link #fail(FailCallback)}
   * @return {@code this} for chaining more calls
   */
  Promise<D> then(Consumer<? super D> doneCallback, Consumer<Throwable> failCallback);

  /**
   * This method will register {@link DoneCallback} so that when a Deferred object
   * is resolved ({@link Deferred#resolve(Object)}), {@link DoneCallback} will be triggered.
   * If the Deferred object is already resolved then the {@link DoneCallback} is triggered immediately.
   *
   * You can register multiple {@link DoneCallback} by calling the method multiple times.
   * The order of callback trigger is based on the order they have been registered.
   *
   * <pre>
   * <code>
   * promise.progress(new DoneCallback(){
   *   public void onDone(Object done) {
   *     ...
   *   }
   * });
   * </code>
   * </pre>
   *
   * @see Deferred#resolve(Object)
   * @param callback the callback to be triggered
   * @return {@code this} for chaining more calls
   */
  Promise<D> done(Consumer<? super D> callback);

  /**
   * This method will register {@link FailCallback} so that when a Deferred object
   * is rejected ({@link Deferred#reject(Object)}), {@link FailCallback} will be triggered.
   * If the Deferred object is already rejected then the {@link FailCallback} is triggered immediately.
   *
   * You can register multiple {@link FailCallback} by calling the method multiple times.
   * The order of callback trigger is based on the order they have been registered.
   *
   * <pre>
   * <code>
   * promise.fail(new FailCallback(){
   *   public void onFail(Object rejection) {
   *     ...
   *   }
   * });
   * </code>
   * </pre>
   *
   * @see Deferred#reject(Object)
   * @param callback the callback to be triggered
   * @return {@code this} for chaining more calls
   */
  Promise<D> fail(Consumer<Throwable> callback);

  /**
   * This method will register {@link AlwaysCallback} so that when a Deferred object is either
   * resolved ({@link Deferred#resolve(Object)}) or rejected ({@link Deferred#reject(Object)}),
   * {@link AlwaysCallback} will be triggered.
   * If the Deferred object is already resolved or rejected then the {@link AlwaysCallback} is
   * triggered immediately.
   *
   * You can register multiple {@link AlwaysCallback} by calling the method multiple times.
   * The order of callback trigger is based on the order they have been registered.
   *
   * {@link AlwaysCallback}s are triggered after any {@link DoneCallback} or {@link FailCallback}
   * respectively.
   *
   * <pre>
   * <code>
   * promise.always(new AlwaysCallback(){
   *   public void onAlways(State state, Object result, Object rejection) {
   *     if (state == State.RESOLVED) {
   *       // do something with result
   *     } else {
   *       // do something with rejection
   *     }
   *   }
   * });
   * </code>
   * </pre>
   *
   * @see Deferred#resolve(Object)
   * @see Deferred#reject(Object)
   * @param callback the callback to be triggered
   * @return {@code this} for chaining more calls
   */
  Promise<D> always(AlwaysCallback<? super D> callback);

  /**
   * This method will wait as long as the State is Pending.  This method will return fast
   * when State is not Pending.
   *
   * @throws InterruptedException if thread is interrupted while waiting
   */
  void waitSafely() throws InterruptedException;

  /**
   * This method will wait when the State is Pending, and return when timeout has reached.
   * This method will return fast when State is not Pending.
   *
   * @param timeout the maximum time to wait in milliseconds
   * @throws InterruptedException if thread is interrupted while waiting
   */
  void waitSafely(long timeout) throws InterruptedException;
}
