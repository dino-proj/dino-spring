// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档文件元数据类，存储文档文件的相关信息
 *
 * @author JL
 */

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonTypeName("DOCUMENT")
public class DocumentFileMeta extends FileMeta {

  @Schema(description = "文档格式，pdf、doc等")
  @Nullable
  private String format;

  @Schema(description = "文档页数")
  @Nullable
  private Integer pages;

  /**
   * 构造文档文件元数据对象。
   * <p>
   * 创建一个新的文档文件元数据实例，文件类型自动设置为DOCUMENT。
   * </p>
   */
  public DocumentFileMeta() {
    // 调用父类构造函数，避免在构造函数中调用可重写的方法
    super(FileTypes.DOCUMENT);
  }

}
