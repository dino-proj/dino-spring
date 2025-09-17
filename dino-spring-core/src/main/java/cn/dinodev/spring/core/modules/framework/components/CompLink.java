// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework.components;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;

import cn.dinodev.spring.commons.data.ValueLabel;
import cn.dinodev.spring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Data
@JsonTypeName(CompLink.T_NAME)
public class CompLink implements Component, Action {
  public static final String T_NAME = "link";

  @Schema(description = "链接的类型", required = true)
  private LinkType linkType;

  @Schema(description = "链接的路径")
  private String path;

  @Schema(description = "链接标题", required = false)
  private String title;

  @Schema(description = "给链接传的参数", required = false)
  private Map<String, String> params;

  @RequiredArgsConstructor
  enum LinkType implements ValueLabel<String> {
    //H5地址
    H5("H5地址"),
    //内部页面
    PAGE("内部页面"),
    //内容
    CONTENT("查看内容");

    private final String label;

    @Override
    public String toString() {
      return getValue();
    }

    @Override
    public String getValue() {
      return this.name().toLowerCase();
    }

    @Override
    public String getLabel() {
      return label;
    }
  }

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
