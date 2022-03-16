package org.dinospring.commons.promise;

import org.dinospring.commons.promise.Promise.State;

/**
 * A callback invoked when the {@code Promise}'s state becomes either {@link Promise.State#RESOLVED} or
 * {@link Promise.State#REJECTED}.
 *
 * @param <D> Type used for {@link Deferred#resolve(Object)}
 * @param <F> Type used for {@link Deferred#reject(Object)}
 *
 * @author Ray Tsang
 * @see Deferred#resolve(Object)
 * @see Deferred#reject(Object)
 * @see Promise#always(AlwaysCallback)
 */
public interface AlwaysCallback<D> {

  /**
   * Invoked when the {@code Promise} resolves or rejects a value.
   *
   * @param state    the state of the {@code Promise}. Either {@link Promise.State#RESOLVED} or {@link Promise.State#REJECTED}
   * @param resolved the resolved value (if any) of the {@code Promise}
   * @param rejected the rejected value (if any) of the {@code Promise}
   */
  void onAlways(final State state, final D resolved, final Throwable rejected);
}
