package org.dinospring.commons.promise;

public interface Defer {
  static <D> Deferred<D> resolve(final D resolve) {
    Deferred<D> defer = new DeferredObject<>();
    defer.resolve(resolve);
    return defer;
  }

  static <D, F extends Throwable> Deferred<D> fail(final F reject) {
    Deferred<D> defer = new DeferredObject<>();
    defer.reject(reject);
    return defer;
  }

  static <D> Deferred<D> fail() {
    Deferred<D> defer = new DeferredObject<>();
    defer.reject(null);
    return defer;
  }
}
