// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Cody Lu
 * @author JL
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldNameConstants
public abstract class VoImplBase<K extends Serializable> implements VoBase<K> {

  @Schema(description = "ID")
  private K id;

  @Schema(description = "创建时间")
  private Date createAt;

  @Schema(description = "最后更新时间")
  private Date updateAt;

  @Schema(description = "状态")
  private String status;

  @Schema(description = "创建用户")
  private String createBy;
}
