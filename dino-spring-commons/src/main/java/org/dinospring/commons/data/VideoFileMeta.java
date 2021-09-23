package org.dinospring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class VideoFileMeta extends FileMeta {

  public VideoFileMeta() {
    this.setType("video");
  }

  @Schema(description = "视频编码格式")
  private String format;

  @Schema(description = "视频宽度")
  private Integer width;

  @Schema(description = "视频高度")
  private Integer height;

  @Schema(description = "视频时长，单位(秒)")
  private Integer duration;
}
