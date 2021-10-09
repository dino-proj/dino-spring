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

package org.dinospring.core.sys.user.wx;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author tuuboo
 */

public interface WxUserInfo {

  /**
   * 用户微信服务号公众号OpenId
   * @return
   */
  @Schema(description = "用户微信服务号公众号OpenId")
  String getWxMpOpenid();

  /**
   * 用户微信UnionId
   * @return
   */
  @Schema(description = "用户微信UnionId")
  String getWxUnionId();

  /**
   * 用户的微信小程序OpenId
   * @return
   */
  @Schema(description = "用户的微信小程序OpenId")
  String getWxMinappOpenid();

  /**
   * 用户的企业微信的userID
   * @return
   */
  @Schema(description = "用户的企业微信的userID，参见 {@link https://work.weixin.qq.com/api/doc/90001/90144/92423}")
  String getWeworkUserid();

  /**
   * 用户的企业微信的openID
   * @return
   */
  @Schema(description = "用户的企业微信的openID，参见 {@link https://work.weixin.qq.com/api/doc/90001/90144/92423}")
  String getWeworkOpenid();

}
