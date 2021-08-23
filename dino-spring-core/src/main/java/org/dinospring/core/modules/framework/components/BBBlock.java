package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBBlock.T_NAME)
public class BBBlock<T extends Component> implements Component {
  public static final String T_NAME = "block";

  @Schema(description = "区块标题")
  private String title;

  @Schema(description = "是否显示标题")
  private boolean showTitle;

  @Schema(description = "是否显示更多")
  private Boolean showMore;

  @Schema(description = "更多的跳转连接")
  private BBLink moreLink;

  @Schema(description = "区块里的内容")
  private T content;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
