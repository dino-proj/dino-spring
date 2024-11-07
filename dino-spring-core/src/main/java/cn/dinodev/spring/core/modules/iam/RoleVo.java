// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.iam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.core.vo.VoImplBase;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cody Lu
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

  @Schema(name = "data_permissions", description = "角色数据权限", required = false)
  private List<String> dataPermissions;

  @Schema(name = "menu_permissions", description = "角色菜单权限", required = false)
  private List<String> menuPermissions;

}
