package org.dinospring.core.modules.framework.components;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeName(BBBanner.T_NAME)
public class BBBanner implements Component {
  public static final String T_NAME = "banner";

  @Schema(description = "轮播间隔时间")
  private Integer interval;

  @Schema(description = "轮播图")
  private List<BBImage> images;

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
