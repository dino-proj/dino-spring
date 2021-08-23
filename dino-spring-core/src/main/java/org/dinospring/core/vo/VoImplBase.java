package org.dinospring.core.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class VoImplBase<K extends Serializable> implements VoBase<K> {

  @Schema(description = "ID")
  private K id;

  @Schema(description = "创建时间")
  private Date createAt;

  @Schema(description = "最后更新时间")
  private Date updateAt;

  @Schema(description = "状态")
  private Integer status;
}
