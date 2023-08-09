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
package org.dinospring.core.modules.iam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dinospring.core.controller.CrudControllerBase;
import org.dinospring.core.modules.iam.RoleControllerBase.RoleReq;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author Cody LU
 * @date 2022-05-04 23:43:44
 */
public interface RoleControllerBase
    extends CrudControllerBase<RoleService, RoleEntity, RoleVo, RoleSearch, RoleReq, Long> {

  /**
   * Vo类的Class
   * @return
   */
  @Nonnull
  @Override
  default Class<RoleVo> voClass() {
    return RoleVo.class;
  }

  /**
   * Entity类的Class
   * @return
   */
  @Nonnull
  @Override
  default Class<RoleEntity> entityClass() {
    return RoleEntity.class;
  }

  @Data
  public class RoleReq {
    @Schema(description = "角色编码", required = true, maxLength = 64)
    @NotBlank
    @Size(max = 64)
    private String code;

    @Schema(description = "角色名称", required = true, maxLength = 64)
    @NotBlank
    @Size(max = 64)
    private String name;

    @Schema(description = "角色备注", required = false, maxLength = 255)
    @Nullable
    @Size(max = 255)
    private String remark;

    @Schema(description = "角色操作权限")
    private List<String> permissions;

    @Schema(name = "menu_permissions", description = "角色菜单权限")
    private List<String> menuPermissions;
  }

}
