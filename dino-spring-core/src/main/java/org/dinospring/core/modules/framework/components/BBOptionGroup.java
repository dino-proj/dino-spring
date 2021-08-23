package org.dinospring.core.modules.framework.components;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBOptionGroup.T_NAME)
public class BBOptionGroup implements Component {
  public static final String T_NAME = "option-group";

  @Schema(description = "组的名字", required = true)
  private String name;

  @Schema(description = "组的图标")
  private String icon;

  @Schema(description = "选项")
  private List<BBOption> options;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }

}
