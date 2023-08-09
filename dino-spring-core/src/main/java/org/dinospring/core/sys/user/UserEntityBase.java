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

import org.dinospring.commons.sys.UserType;
import org.dinospring.data.domain.EntityBase;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@MappedSuperclass
@FieldNameConstants
public abstract class UserEntityBase<K extends Serializable> extends EntityBase<K> {
  @Schema(description = "用户类型")
  @Column(name = "user_type", nullable = false, columnDefinition = "varchar(8)")
  UserType userType;

  @Schema(description = "用户登录ID")
  @Column(name = "login_name", nullable = true, length = 20)
  String loginName;

  @Schema(description = "用户头像 url")
  @Column(name = "avatar_url", length = 2048)
  String avatarUrl;

  @Schema(description = "用户密码Hash")
  @Column(name = "password_hash", length = 128)
  @JsonIgnore
  String passwordHash;

  @Schema(description = "用户昵称")
  @Column(name = "nick_name", length = 20)
  String nickName;

  @Schema(description = "用户真实姓名")
  @Column(name = "real_name", length = 20)
  String realName;

  @Schema(description = "用户手机号")
  @Column(name = "mobile", nullable = true, length = 16)
  String mobile;

  @Schema(description = "用户私钥")
  @Column(name = "secret_key", nullable = false, length = 64)
  String secretKey;

  @Schema(description = "用户最后登录时间")
  @Column(name = "last_login_at", nullable = true)
  Date lastLoginAt;
}
