package cn.dinodev.spring.commons.promise;

/**
 * Deferred interface to trigger an event (resolve, reject, notify).
 * Subsequently, this will allow Promise observers to listen in on the event
 * (done, fail, progress).
 *
 * @param <D> Type used for resolved value
 *
 * @author Ray Tsang
 * @see DeferredObject
 */
public interface Deferred<D> extends Promise<D> {
  /**
   * This should be called when a task has completed successfully.
   *
   * <pre>
   * <code>
   * Deferred deferredObject = new DeferredObject();
   * Promise promise = deferredObject.promise();
   * promise.done(result -&gt; {
   *   // Done!
   * });
   *
   * // another thread using the same deferredObject
   * deferredObject.resolve("OK");
   *
   * </code>
   * </pre>
   *
   * @param resolve the resolved value for this {@code Deferred}
   *
   * @return the reference to this {@code Deferred} instance.
   */
  Deferred<D> resolve(D resolve);

  /**
   * This should be called when a task has completed unsuccessfully,
   * i.e., a failure may have occurred.
   *
   * <pre>
   * <code>
   * Deferred deferredObject = new DeferredObject();
   * Promise promise = deferredObject.promise();
   * promise.fail(error -&gt; {
   *   // Failed :(
   * });
   *
   * // another thread using the same deferredObject
   * deferredObject.reject("BAD");
   *
   * </code>
   * </pre>
   *
   * @param <F> Type of the exception/error
   * @param reject the rejected value for this {@code Deferred}
   *
   * @return the reference to this {@code Deferred} instance.
   */
  <F extends Throwable> Deferred<D> reject(F reject);

  /**
   * Return an {@link Promise} instance (i.e., an observer).  You can register callbacks in this observer.
   *
   * @return the reference to this {@code Deferred} instance as a {@code Promise},
   */
  Promise<D> promise();
}