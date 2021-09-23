package org.dinospring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ImageFileMeta extends FileMeta {

  public ImageFileMeta() {
    this.setType("image/*");
  }

  @Schema(description = "图片编码格式")
  private String format;

  @Schema(description = "图片宽度")
  private Integer width;

  @Schema(description = "图片高度")
  private Integer height;
}
