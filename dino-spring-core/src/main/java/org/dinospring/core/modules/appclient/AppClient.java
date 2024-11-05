// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.appclient;

import java.util.ArrayList;
import java.util.List;

import org.dinospring.commons.sys.Tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody LU
 */

@Data
public class AppClient {

  @Schema(description = "app客户端ID")
  String id;

  @Schema(description = "app客户端名称")
  String name;

  @Schema(description = "客户端绑定的租户")
  List<Tenant> bindTenants = new ArrayList<>();
}
