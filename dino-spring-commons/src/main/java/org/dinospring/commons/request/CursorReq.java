// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.request;

import org.springdoc.core.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 滑动请求信息
 * @author Cody Lu
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
@Schema(description = "滑动请求信息")
public class CursorReq {

  /**
   * The Cursor.
   */
  @Nullable
  @Parameter(description = "起始游标，null表示从头开始")
  private String cursor;

  /**
   * The Size.
   */
  @Min(1)
  @Parameter(description = "页长，最小为1", schema = @Schema(type = "integer", defaultValue = "10"))
  private Integer pl = 10;

}
