package org.dinospring.core.modules.framework.components;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;

public interface Component extends Serializable {
  @Schema(description = "组件名称")
  @JsonProperty(value = "@t", access = Access.READ_ONLY)
  String getComponentName();
}
