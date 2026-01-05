// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.core.modules.iam;

import java.util.List;

import cn.dinodev.spring.core.controller.CrudControllerBase;
import cn.dinodev.spring.core.modules.iam.RoleControllerBase.RoleReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Cody Lu
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

  /**
   * 角色请求对象
   */
  @Data
  class RoleReq {
    @Schema(description = "角色编码", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 64)
    @NotBlank
    @Size(max = 64)
    private String code;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 64)
    @NotBlank
    @Size(max = 64)
    private String name;

    @Schema(description = "角色备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
    @Nullable
    @Size(max = 255)
    private String remark;

    @Schema(description = "角色操作权限")
    private List<String> permissions;

    @Schema(name = "menu_permissions", description = "角色菜单权限")
    private List<String> menuPermissions;
  }

}
