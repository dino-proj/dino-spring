// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package org.dinospring.auth.exception;

/**
 * 权限异常，当权限不足时抛出
 * @author Cody Lu
 * @date 2022-04-06 23:04:04
 */

public class AuthorizationException extends RuntimeException {

  /**
   * Creates a new AuthorizationException.
   */
  public AuthorizationException() {
    super();
  }

  /**
   * Constructs a new AuthorizationException.
   *
   * @param message the reason for the exception
   */
  public AuthorizationException(String message) {
    super(message);
  }

  /**
   * Constructs a new AuthorizationException.
   *
   * @param cause the underlying Throwable that caused this exception to be thrown.
   */
  public AuthorizationException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new AuthorizationException.
   *
   * @param message the reason for the exception
   * @param cause   the underlying Throwable that caused this exception to be thrown.
   */
  public AuthorizationException(String message, Throwable cause) {
    super(message, cause);
  }
}
