package org.dinospring.core.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

public interface VoBase<K extends Serializable> extends Serializable {

  @Schema(description = "ID")
  K getId();

  @Schema(description = "创建时间")
  Date getCreateAt();

  @Schema(description = "最后更新时间")
  Date getUpdateAt();

  @Schema(description = "状态")
  Integer getStatus();
}
