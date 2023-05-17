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
package org.dinospring.core.modules.openim;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Response;
import org.dinospring.core.modules.openim.restapi.UserToken;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;

/**
 * OpenIm模块的基础控制器
 * @author tuuboo
 * @date 2022-04-13 03:09:25
 */

public interface OpenimControllerBase {

  /**
   * 获取当前用户的openim ID
   *
   * @return
   */
  String currentUserImId();

  /**
   * 获取当前用户的Token
   *
   * @param platform 平台
   * @return 用户Token
   */
  @Operation(summary = "获取当前用户的Openim Token")
  @GetMapping("/auth")
  default Response<UserToken> auth(int platform) {
    var service = ContextHelper.findBean(OpenimService.class);
    return Response.success(service.getUserToken(currentUserImId(), platform));
  }
}
