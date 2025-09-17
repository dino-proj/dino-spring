// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import cn.dinodev.spring.commons.json.annotation.JsonDiscriminator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */
@Data
@JsonInclude(Include.NON_NULL)
@JsonDiscriminator(property = "type")
public class FileMeta implements Serializable {

  @Schema(description = "文件类型")
  private FileTypes type;

  @Schema(description = "文件存放桶")
  private String bucket;

  @Schema(description = "文件存放路径")
  private String path;

  @Schema(description = "文件大小")
  private Long size;
}
