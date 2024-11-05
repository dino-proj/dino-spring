// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.commons.data.FileMeta;
import org.dinospring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody LU
 */

@Data
@JsonTypeName(CompImage.T_NAME)
public class CompImage implements Component {
  public static final String T_NAME = "image";

  @Schema(description = "图片URL地址")
  private FileMeta imageUrl;

  @Schema(description = "图片标题")
  private String title;

  @Schema(description = "图片链接")
  private CompLink link;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
