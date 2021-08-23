package org.dinospring.core.modules.framework;

import org.dinospring.core.modules.framework.template.Template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Page<T extends PageConfig> {
  @Schema(description = "页面ID")
  private Long id;

  @Schema(description = "页面标题")
  private String title;

  @Schema(description = "页面对应的模板")
  private Template template;

  @Schema(description = "页面的配置属性", type = "json")
  private T properties;
}
