package org.dinospring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class FileMeta {

  @Schema(description = "文件类型")
  private String type;

  @Schema(description = "文件存放路径")
  private String path;

  @Schema(description = "文件大小")
  private Long size;
}
