// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
