package org.dinospring.core.sys.config;

import io.swagger.v3.oas.annotations.media.Schema;

public interface Property {
  Long getId();

  String getScope();

  String getKey();

  @Schema(implementation = Object.class, type = "json", description = "可以是原始类型，比如数字、字符串、布尔等，也可以是数组、json对象")
  Object getValue();
}
