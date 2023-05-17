// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author tuuboo
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("VIDEO")
public class VideoFileMeta extends FileMeta {

  public VideoFileMeta() {
    this.setType(FileTypes.VIDEO);
  }

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
}
