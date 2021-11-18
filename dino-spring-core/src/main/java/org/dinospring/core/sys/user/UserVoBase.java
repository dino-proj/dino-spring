/*
 *  Copyright 2021 dinospring.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dinospring.core.sys.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dinospring.commons.sys.UserType;
import org.dinospring.core.vo.VoImplBase;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JL
 * @Date: 2021/11/10
 */
@Data
public class UserVoBase<K extends Serializable> extends VoImplBase<K> implements UserBase<K> {

  @Schema(name = "nick_name", description = "用户昵称")
  private String nickName;

  @Schema(name = "real_name", description = "用户真实姓名")
  private String realName;

  @Schema(name = "mobile", description = "用户手机号")
  private String mobile;

  @Schema(name = "lastLogin_at", description = "用户最后登录时间")
  private Date lastLoginAt;

  @Schema(name = "guid", description = "用户设备ID")
  private String guid;

  @Schema(name = "tenant_id", description = "用户所属租户ID")
  private String tenantId;

  @Schema(name = "login_name", description = "用户登录ID")
  private String loginName;

  @Schema(name = "avatar_url", description = "用户头像 url")
  private String avatarUrl;

  @Schema(name = "secret_key", description = "用户的SecretKey")
  private String secretKey;

  @Schema(name = "user_type", description = "用户类型")
  private UserType userType;
}
