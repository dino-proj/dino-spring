package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBButton.T_NAME)
public class BBButton implements Component {
  public static final String T_NAME = "button";

  @Schema(description = "按钮标签")
  private String label;

  @Schema(description = "按钮的图标")
  private String icon;

  @Schema(description = "执行动作", oneOf = { BBLink.class, BBAlert.class })
  private Action action;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
