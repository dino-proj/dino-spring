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

package org.dinospring.core.sys.user;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.dinospring.commons.sys.UserType;

/**
 *
 * @author tuuboo
 */

public enum DefaultUserType implements UserType {
  //系统用户
  SYS("sys"),
  //后台管理用户
  ADMIN("admin"),
  //前端用户
  CLIENT("client"),
  //api用户
  API("api"),
  //访客用户
  GUEST("guest");

  private String userType;

  private DefaultUserType(String userType) {
    this.userType = userType;
  }

  @Override
  public String getType() {
    return userType;
  }

  @Override
  public String toString() {
    return this.getType();
  }

  @Override
  public List<UserType> allTypes() {
    return Arrays.stream(DefaultUserType.values()).collect(Collectors.toList());
  }

  public static UserType of(String userType) {
    return DefaultUserType.valueOf(userType.toUpperCase());
  }
}
