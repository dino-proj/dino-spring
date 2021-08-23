package org.dinospring.core.modules.framework.components;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.commons.ValueLabel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@JsonTypeName(BBLink.T_NAME)
public class BBLink implements Component, Action {
  public static final String T_NAME = "link";

  @Schema(description = "链接的类型", required = true)
  private LinkType linkType;

  @Schema(description = "链接的路径", required = true)
  private String path;

  @Schema(description = "链接标题", required = false)
  private String title;

  @Schema(description = "给链接传的参数", required = false)
  private Map<String, String> params;

  @RequiredArgsConstructor
  enum LinkType implements ValueLabel<String> {
    H5("H5地址"), PAGE("内部页面"), CONTENT("查看内容");

    private final String label;

    @Override
    public String toString() {
      return getValue();
    }

    @Override
    public String getValue() {
      return this.name().toLowerCase();
    }

    @Override
    public String getLabel() {
      return label;
    }
  }

  @Schema(title = "@t", description = "组件名字:" + T_NAME)
  @Override
  public String getComponentName() {
    return T_NAME;
  }
}
