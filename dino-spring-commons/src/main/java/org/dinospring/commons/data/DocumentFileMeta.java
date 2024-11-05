// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author JL
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("DOCUMENT")
public class DocumentFileMeta extends FileMeta {

  public DocumentFileMeta() {
    super.setType(FileTypes.DOCUMENT);
  }

  @Schema(description = "文档格式，pdf、doc等")
  @Nullable
  private String format;

  @Schema(description = "文档页数")
  @Nullable
  private Integer pages;

}
