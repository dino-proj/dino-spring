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
 * 视频文件元数据类，包含视频文件的特有属性信息
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("VIDEO")
public class VideoFileMeta extends FileMeta {

  @Schema(description = "视频文件格式")
  private String format;

  @Schema(description = "视频宽度")
  private Integer width;

  @Schema(description = "视频高度")
  private Integer height;

  @Schema(description = "视频时长，单位(秒)")
  private Long duration;

  @Schema(description = "视频画质", allowableValues = { "240", "360", "480", "720", "1080", "2k", "4k", "8k" })
  private String resolution;

  /**
   * 构造视频文件元数据对象。
   * <p>
   * 创建一个新的视频文件元数据实例，文件类型自动设置为VIDEO。
   * </p>
   */
  public VideoFileMeta() {
    // 调用父类构造函数，避免在构造函数中调用可重写的方法
    super(FileTypes.VIDEO);
  }
}
