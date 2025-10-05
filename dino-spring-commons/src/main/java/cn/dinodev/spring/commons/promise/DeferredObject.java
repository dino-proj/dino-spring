package cn.dinodev.spring.commons.promise;

/**
 * An implementation of {@link Deferred} interface.
 *
 * <pre>
 * <code>
 * final Deferred deferredObject = new DeferredObject();
 *
 * Promise promise = deferredObject.promise();
 * promise
 *   .done(result -&gt; { ... })
 *   .fail(error -&gt; { ... });
 *
 * Runnable runnable = new Runnable() {
 *   public void run() {
 *     int sum = 0;
 *     for (int i = 0; i &lt; 100; i++) {
 *       // something that takes time
 *       sum += i;
 *     }
 *     deferredObject.resolve(sum);
 *   }
 * }
 * // submit the task to run
 *
 * </code>
 * </pre>
 *
 * @param <D> Type of the resolved value
 * @author Ray Tsang
 */
public class DeferredObject<D> extends AbstractPromise<D> implements Deferred<D> {
  @Override
  public Deferred<D> resolve(final D resolve) {
    lock.lock();
    try {
      if (!isPending()) {
        throw new IllegalStateException("Deferred object already finished, cannot resolve again");
      }

      this.promiseState = State.RESOLVED;
      this.resolveResult = resolve;

      try {
        triggerDone(resolve);
      } finally {
        triggerAlways(promiseState, resolve, null);
      }
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public Deferred<D> reject(final Throwable reject) {
    lock.lock();
    try {
      if (!isPending()) {
        throw new IllegalStateException("Deferred object already finished, cannot reject again");
      }
      this.promiseState = State.REJECTED;
      this.rejectResult = reject;

      try {
        triggerFail(reject);
      } finally {
        triggerAlways(promiseState, null, reject);
      }
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public Promise<D> promise() {
    return this;
  }

}