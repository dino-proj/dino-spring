// Copyright 2021 dinospring.cn
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

package org.dinospring.core.sys.token;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data
@Builder
public class TokenPrincaple implements Serializable {

  @Schema(description = "租户ID")
  private String tenantId;

  @Schema(description = "用户类型")
  private String userType;

  @Schema(description = "用户ID")
  private String userId;

  @Schema(description = "平台")
  private String plt;

  @Schema(description = "设备ID")
  private String guid;
}
