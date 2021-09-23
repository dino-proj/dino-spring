package org.dinospring.core.modules.oss;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketMeta {

  @Schema(description = "Bucket名字")
  private String name;

  @Schema(description = "Bucket创建时间")
  private Date createAt;

  public static BucketMeta of(String name, Date createAt) {
    return new BucketMeta(name, createAt);
  }
}
