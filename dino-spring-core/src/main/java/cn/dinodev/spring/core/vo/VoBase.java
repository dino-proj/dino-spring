// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
 * @author JL
 */
public interface VoBase<K extends Serializable> extends Serializable {

  /**
   * ID
   * @return id
   */
  @Schema(description = "ID")
  K getId();

  /**
   * 对象创建时间
   * @return 创建时间
   */
  @Schema(description = "创建时间")
  Date getCreateAt();

  /**
   * 对象最后更新时间
   * @return 更新时间
   */
  @Schema(description = "最后更新时间")
  Date getUpdateAt();

  /**
   * 对象状态码
   * @return
   */
  @Schema(description = "状态码")
  String getStatus();

  /**
   * 创建用户
   * @return
   */
  @Schema(description = "创建用户")
  String getCreateBy();
}
