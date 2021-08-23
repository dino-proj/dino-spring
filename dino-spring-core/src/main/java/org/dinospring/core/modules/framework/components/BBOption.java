package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.commons.ValueLabel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBOption.T_NAME)
public class BBOption implements Component, ValueLabel<String> {
  public static final String T_NAME = "option";

  @Schema(description = "选项值", required = true)
  private String value;

  @Schema(description = "选项标签", required = true)
  private String label;

  @Schema(description = "选项的图标")
  private String icon;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }

}
