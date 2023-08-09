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
