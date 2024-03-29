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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * Post请求包装
 * @author Cody LU
 */

@Data
public class PostBody<T> {
  @Schema(description = "设备GUID", nullable = true)
  private String guid;

  @Schema(description = "客户端的平台，PC、APP、WX、H5等")
  private String plt;

  @Schema(description = "Session ID", nullable = true)
  private String sid;

  @Schema(description = "Post数据")
  @Valid
  private T body;
}
