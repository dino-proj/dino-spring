package org.dinospring.core.modules.framework.template;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.dinospring.core.modules.framework.PageType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Template {

  @Schema(description = "模板ID")
  private String name;

  @Schema(description = "模板标题")
  private String title;

  @Schema(description = "模板图标")
  private String icon;

  @Schema(description = "页面APP端路径")
  private String appPath;

  @Schema(description = "页面pc端路径")
  private String pcPath;

  @Schema(description = "对模板的描述")
  private String description;

  @Schema(description = "页面类型")
  private PageType type;

  @JsonIgnore
  private Class<?> confClass;
}
