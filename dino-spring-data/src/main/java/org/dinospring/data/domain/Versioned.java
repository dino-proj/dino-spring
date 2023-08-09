package org.dinospring.data.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;

/**
 * @author Cody LU
 */
public interface Versioned {

  /**
   * 版本号
   * @return
   */
  @Schema(description = "版本号")
  @Column(name = "version", nullable = true)
  Long getVersion();
}
