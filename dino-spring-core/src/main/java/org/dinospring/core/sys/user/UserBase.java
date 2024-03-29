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

package org.dinospring.core.sys.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.dinospring.commons.sys.User;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody LU
 */
public interface UserBase<K extends Serializable> extends User<K> {

  /**
   * 用户昵称
   * @return
   */
  @Schema(description = "用户昵称")
  String getNickName();

  /**
   * 用户真实姓名
   * @return
   */
  @Schema(description = "用户真实姓名")
  String getRealName();

  /**
   * 用户手机号
   * @return
   */
  @Schema(description = "用户手机号")
  String getMobile();

  /**
   * 用户最后登录时间
   * @return
   */
  @Schema(description = "用户最后登录时间")
  Date getLastLoginAt();

  /**
   * 用户显示名，如果有昵称则显示昵称，没有则显示真实名字
   * @return
   */
  @Schema(description = "用户显示名，如果有昵称则显示昵称，没有则显示真实名字")
  @Override
  default String getDisplayName() {
    return Objects.toString(this.getNickName(), this.getRealName());
  }
}
