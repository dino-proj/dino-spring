// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.user.wx;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
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
