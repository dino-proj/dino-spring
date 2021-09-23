package org.dinospring.core.modules.oss;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectMeta {

  @Schema(description = "对象名字")
  private String name;

  private boolean dir;

  @Schema(description = "对象大小")
  @Builder.Default
  private long size = 0;

  @Schema(description = "最后修改时间")
  private Date updateAt;

  @Schema(description = "是否是文件夹")
  public boolean isDir() {
    return dir;
  }

  @Schema(description = "是否是文件")
  public boolean isFile() {
    return !dir;
  }
}
