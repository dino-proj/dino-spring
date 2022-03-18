// Copyright 2021 dinospring.cn
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

package org.dinospring.commons.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dinospring.commons.response.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用的业务异常类 BusinessException
 * @author : luxueyu
 * @version : v1.0
 * @since 2021-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

  /**
   * 错误编码
   */
  private final int code;

  private final transient Object data;

  protected BusinessException(int code, String msg, Object data, Throwable cause) {
    super(msg, cause);
    this.code = code;
    this.data = data;
  }

  public static BusinessException of(@Nonnull Status status) {
    return new BusinessException(status.getCode(), status.getMsg(), null, null);
  }

  public static BusinessException of(@Nonnull Status status, @Nonnull Object data) {
    return new BusinessException(status.getCode(), status.getMsg(), data, null);
  }

  public static BusinessException of(@Nonnull Status status, @Nullable Throwable cause) {
    return new BusinessException(status.getCode(), status.getMsg(), null, cause);
  }
}
