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

package org.dinospring.commons.request;

import org.springdoc.core.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 滑动请求信息
 * @author Cody LU
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
@Schema(description = "滑动请求信息")
public class CursorReq {

  /**
   * The Cursor.
   */
  @Nullable
  @Parameter(description = "起始游标，null表示从头开始")
  private String cursor;

  /**
   * The Size.
   */
  @Min(1)
  @Parameter(description = "页长，最小为1", schema = @Schema(type = "integer", defaultValue = "10"))
  private Integer pl = 10;

}
