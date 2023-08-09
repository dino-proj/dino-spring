package org.dinospring.commons.promise;

/**
 * @author Cody LU
 */
public interface Defer {

  /**
   * 解析
   * @param resolve
   * @param <D>
   * @return
   */
  static <D> Deferred<D> resolve(final D resolve) {
    Deferred<D> defer = new DeferredObject<>();
    defer.resolve(resolve);
    return defer;
  }

  /**
   * 失败
   * @param reject
   * @param <D>
   * @param <F>
   * @return
   */
  static <D, F extends Throwable> Deferred<D> fail(final F reject) {
    Deferred<D> defer = new DeferredObject<>();
    defer.reject(reject);
    return defer;
  }

  /**
   * 失败
   * @param <D>
   * @return
   */
  static <D> Deferred<D> fail() {
    Deferred<D> defer = new DeferredObject<>();
    defer.reject(null);
    return defer;
  }
}
