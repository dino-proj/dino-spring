// Copyright 2022 dinodev.cn
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

package org.dinospring.core.modules.iam;

import java.io.Serializable;

import lombok.extern.jackson.Jacksonized;
import org.dinospring.commons.data.ValueLabel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Cody LU
 * @date 2022-04-12 14:21:19
 */

@Data
@Builder
@Jacksonized
public class Action implements ValueLabel<String>, Serializable {

  @Schema(description = "权限：如user:create，多个操作用逗号分隔，如user:create,update")
  private String value;

  @Schema(description = "权限名字：如 “创建")
  private String label;

}
