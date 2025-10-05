// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图像文件元数据类，包含图像文件的特有属性信息
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("IMAGE")
public class ImageFileMeta extends FileMeta {

  @Schema(description = "图片编码格式")
  private String format;

  @Schema(description = "图片宽度")
  private Integer width;

  @Schema(description = "图片高度")
  private Integer height;

  /**
   * 构造图像文件元数据对象。
   * <p>
   * 创建一个新的图像文件元数据实例，文件类型自动设置为IMAGE。
   * </p>
   */
  public ImageFileMeta() {
    // 调用父类构造函数，避免在构造函数中调用可重写的方法
    super(FileTypes.IMAGE);
  }
}
