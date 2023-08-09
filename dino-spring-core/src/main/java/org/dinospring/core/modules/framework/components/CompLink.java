// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.framework.components;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.commons.data.ValueLabel;
import org.dinospring.core.modules.framework.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Cody LU
 */

@Data
@JsonTypeName(CompLink.T_NAME)
public class CompLink implements Component, Action {
  public static final String T_NAME = "link";

  @Schema(description = "链接的类型", required = true)
  private LinkType linkType;

  @Schema(description = "链接的路径")
  private String path;

  @Schema(description = "链接标题", required = false)
  private String title;

  @Schema(description = "给链接传的参数", required = false)
  private Map<String, String> params;

  @RequiredArgsConstructor
  enum LinkType implements ValueLabel<String> {
    //H5地址
    H5("H5地址"),
    //内部页面
    PAGE("内部页面"),
    //内容
    CONTENT("查看内容");

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
