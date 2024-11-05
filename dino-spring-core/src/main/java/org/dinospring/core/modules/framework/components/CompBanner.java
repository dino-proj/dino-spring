// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework.components;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody LU
 */

@Data
@JsonTypeName(CompBanner.T_NAME)
public class CompBanner implements Component {
  public static final String T_NAME = "banner";

  @Schema(description = "轮播间隔时间")
  private Integer interval;

  @Schema(description = "轮播图")
  private List<CompImage> images;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
