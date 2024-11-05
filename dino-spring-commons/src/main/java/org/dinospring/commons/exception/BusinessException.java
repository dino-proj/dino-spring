// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.exception;

import org.dinospring.commons.response.Status;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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
