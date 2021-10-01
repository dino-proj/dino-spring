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

package org.dinospring.core.sys.org;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.sys.User;

import io.swagger.v3.oas.annotations.media.Schema;

public interface UserBase<K extends Serializable> extends User<K> {

  @Schema(description = "用户昵称")
  String getNickName();

  @Schema(description = "用户真实姓名")
  String getRealName();

  @Schema(description = "用户手机号")
  String getMobile();

  @Schema(description = "用户微信服务号公众号OpenId")
  String getWxMpOpenid();

  @Schema(description = "用户的微信小程序OpenId")
  String getWxMinappOpenid();

  @Schema(description = "用户的企业微信的userID，参见 {@link https://work.weixin.qq.com/api/doc/90001/90144/92423}")
  String getWeworkUserid();

  @Schema(description = "用户的企业微信的openID，参见 {@link https://work.weixin.qq.com/api/doc/90001/90144/92423}")
  String getWeworkOpenid();

  @Schema(description = "用户最后登录时间")
  Date getLastLoginAt();

  @Schema(description = "用户显示名，如果有昵称则显示昵称，没有则显示真实名字")
  @Override
  default String getDisplayName() {
    return StringUtils.defaultString(getNickName(), getRealName());
  }
}
