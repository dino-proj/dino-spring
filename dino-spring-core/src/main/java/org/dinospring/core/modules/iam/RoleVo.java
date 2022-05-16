// Copyright 2022 dinospring.cn
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

package org.dinospring.core.modules.iam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dinospring.commons.sys.User;
import org.dinospring.core.vo.VoImplBase;

import java.io.Serializable;
import java.util.List;

/**
 * @author tuuboo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleVo extends VoImplBase<Long> {

  @Schema(description = "角色编码")
  private String code;

  @Schema(description = "角色名字")
  private String name;

  @Schema(description = "角色描述")
  private String remark;

  @Schema(description = "包含的权限")
  private List<Action> actions;

  @Schema(description = "包含的用户")
  private List<User<Serializable>> users;

  @Schema(description = "角色操作权限", required = false)
  private List<String> permissions;

  @Schema(description = "角色数据权限", required = false)
  private List<String> dataPermissions;

  @Schema(description = "角色菜单权限", required = false)
  private List<String> menuPermissions;

}
