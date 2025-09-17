// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.auth.exception;

/**
 * 权限异常，当权限不足时抛出
 * @author Cody Lu
 * @date 2022-04-06 23:04:04
 */

public class AuthorizationException extends RuntimeException {

  /**
   * 创建一个新的 AuthorizationException。
   */
  public AuthorizationException() {
    super();
  }

  /**
   * 构造一个新的 AuthorizationException。
   *
   * @param message 异常原因
   */
  public AuthorizationException(String message) {
    super(message);
  }

  /**
   * 构造一个新的 AuthorizationException。
   *
   * @param cause 导致此异常被抛出的底层 Throwable。
   */
  public AuthorizationException(Throwable cause) {
    super(cause);
  }

  /**
   * 构造一个新的 AuthorizationException。
   *
   * @param message 异常原因
   * @param cause   导致此异常被抛出的底层 Throwable。
   */
  public AuthorizationException(String message, Throwable cause) {
    super(message, cause);
  }
}
