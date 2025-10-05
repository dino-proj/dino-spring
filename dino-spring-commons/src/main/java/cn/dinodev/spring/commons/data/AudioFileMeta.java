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
 * 音频文件元数据类，存储音频文件的相关信息
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("AUDIO")
public class AudioFileMeta extends FileMeta {

  @Schema(description = "音频文件格式")
  private String format;

  @Schema(description = "音频时长，单位(秒)")
  private Long duration;

  /**
   * 构造音频文件元数据对象。
   * <p>
   * 创建一个新的音频文件元数据实例，文件类型自动设置为AUDIO。
   * </p>
   */
  public AudioFileMeta() {
    // 调用父类构造函数，避免在构造函数中调用可重写的方法
    super(FileTypes.AUDIO);
  }
}
