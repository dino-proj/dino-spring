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
 *
 *
 * <p>该类用于表示业务逻辑中的异常情况，包含错误编码和附加数据。</p>
 *
 * <p>字段：</p>
 * <ul>
 *   <li>code：错误编码</li>
 *   <li>data：附加数据，使用 transient 修饰，表示该字段不会被序列化</li>
 * </ul>
 *
 * <p>构造方法：</p>
 * <ul>
 *   <li>BusinessException(int code, String msg, Object data, Throwable cause)：受保护的构造方法，用于初始化错误编码、错误信息、附加数据和异常原因</li>
 * </ul>
 *
 * <p>静态工厂方法：</p>
 * <ul>
 *   <li>of(Status status)：根据状态创建 BusinessException 实例，不包含附加数据和异常原因</li>
 *   <li>of(Status status, Object data)：根据状态和附加数据创建 BusinessException 实例，不包含异常原因</li>
 *   <li>of(Status status, Throwable cause)：根据状态和异常原因创建 BusinessException 实例，不包含附加数据</li>
 * </ul>
 *
 * @author : luxueyu
 * @version : v1.0
 * @since 2021-06-20
 *
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

  /**
   * 根据状态创建 BusinessException 实例，不包含附加数据和异常原因
   * @param status 状态
   * @return BusinessException 实例
   */
  public static BusinessException of(@Nonnull Status status) {
    return new BusinessException(status.getCode(), status.getMsg(), null, null);
  }

  /**
   * 根据状态和附加数据创建 BusinessException 实例，不包含异常原因
   * @param status 状态
   * @param data 附加数据
   * @return BusinessException 实例
   */
  public static BusinessException of(@Nonnull Status status, @Nonnull Object data) {
    return new BusinessException(status.getCode(), status.getMsg(), data, null);
  }

  /**
   * 根据状态、附加数据和异常原因创建 BusinessException 实例
   * @param status 状态
   * @param data 附加数据
   * @param cause 异常原因
   * @return BusinessException 实例
   */
  public static BusinessException of(@Nonnull Status status, @Nonnull Object data, @Nullable Throwable cause) {
    return new BusinessException(status.getCode(), status.getMsg(), data, cause);
  }

  public static BusinessException of(@Nonnull Status status, @Nullable Throwable cause) {
    return new BusinessException(status.getCode(), status.getMsg(), null, cause);
  }
}
