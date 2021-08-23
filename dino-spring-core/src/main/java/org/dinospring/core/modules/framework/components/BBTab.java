package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBTab.T_NAME)
public class BBTab<T extends Component> implements Component {
  public static final String T_NAME = "tab";

  @Schema(description = "Tab项标签")
  private String label;

  @Schema(description = "Tab项的图标")
  private String icon;

  @Schema(description = "Tab页里的内容")
  private T content;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }

}
