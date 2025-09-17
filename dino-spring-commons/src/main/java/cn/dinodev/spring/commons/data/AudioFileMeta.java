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
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("AUDIO")
public class AudioFileMeta extends FileMeta {

  public AudioFileMeta() {
    super.setType(FileTypes.AUDIO);
  }

  @Schema(description = "音频文件格式")
  private String format;

  @Schema(description = "音频时长，单位(秒)")
  private Long duration;
}
