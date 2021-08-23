package org.dinospring.core.modules.framework.components;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBImage.T_NAME)
public class BBImage implements Component {
  public static final String T_NAME = "image";

  @Schema(description = "图片URL地址")
  private String imageUrl;

  @Schema(description = "图片标题")
  private String title;

  @Schema(description = "图片链接")
  private BBLink link;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
