package cn.dinodev.spring.commons.promise;

/**
 * An implementation of {@link Deferred} interface.
 * <p>
 * <pre>
 * <code>
 * final {@link Deferred} deferredObject = new {@link DeferredObject}
 *
 * {@link Promise} promise = deferredObject.promise();
 * promise
 *   .done(new DoneCallback() { ... })
 *   .fail(new FailCallback() { ... })
 *   .progress(new ProgressCallback() { ... });
 *
 * {@link Runnable} runnable = new {@link Runnable}() {
 *   public void run() {
 *     int sum = 0;
 *     for (int i = 0; i < 100; i++) {
 *       // something that takes time
 *       sum += i;
 *       deferredObject.notify(i);
 *     }
 *     deferredObject.resolve(sum);
 *   }
 * }
 * // submit the task to run
 *
 * </code>
 * </pre>
 *
 * @author Ray Tsang
 * @see DoneCallback
 * @see FailCallback
 */
public class DeferredObject<D> extends AbstractPromise<D> implements Deferred<D> {
  @Override
  public Deferred<D> resolve(final D resolve) {
    synchronized (this) {
      if (!isPending()) {
        throw new IllegalStateException("Deferred object already finished, cannot resolve again");
      }

      this.state = State.RESOLVED;
      this.resolveResult = resolve;

      try {
        triggerDone(resolve);
      } finally {
        triggerAlways(state, resolve, null);
      }
    }
    return this;
  }

  @Override
  public Deferred<D> reject(final Throwable reject) {
    synchronized (this) {
      if (!isPending()) {
        throw new IllegalStateException("Deferred object already finished, cannot reject again");
      }
      this.state = State.REJECTED;
      this.rejectResult = reject;

      try {
        triggerFail(reject);
      } finally {
        triggerAlways(state, null, reject);
      }
    }
    return this;
  }

  @Override
  public Promise<D> promise() {
    return this;
  }

}