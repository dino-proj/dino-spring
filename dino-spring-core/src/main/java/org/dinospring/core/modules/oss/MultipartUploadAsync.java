package org.dinospring.core.modules.oss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author: Jack Liu
 * @Date: 2022/10/25 11:21
 */

@Data
public class MultipartUploadAsync {

  @Schema(name = "upload_id", description = "上传id")
  private String uploadId;

  @Schema(name = "file_name", description = "文件全名，带后缀")
  private String fileName;

  @Schema(name = "chunks", description = "分片信息")
  private List<MultipartUploadChunk> chunks;

  @Data
  public static class MultipartUploadChunk {

    @Schema(name = "part_number", description = "分片编号")
    private Integer partNumber;

    @Schema(name = "part_number", description = "上传地址")
    private String uploadUrl;
  }
}
