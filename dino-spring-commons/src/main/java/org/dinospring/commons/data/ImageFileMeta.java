// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("IMAGE")
public class ImageFileMeta extends FileMeta {

  public ImageFileMeta() {
    this.setType(FileTypes.IMAGE);
  }

  @Schema(description = "图片编码格式")
  private String format;

  @Schema(description = "图片宽度")
  private Integer width;

  @Schema(description = "图片高度")
  private Integer height;
}
