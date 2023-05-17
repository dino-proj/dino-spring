// Copyright 2022 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.dinospring.auth.exception;

/**
 * 权限异常，当权限不足时抛出
 * @author tuuboo
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
