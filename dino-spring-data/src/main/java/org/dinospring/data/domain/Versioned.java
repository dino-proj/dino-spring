package org.dinospring.data.domain;

import javax.persistence.Column;

import io.swagger.v3.oas.annotations.media.Schema;

public interface Versioned {

  /**
   * 版本号
   * @return
   */
  @Schema(description = "版本号")
  @Column(name = "version", nullable = true)
  Long getVersion();
}
