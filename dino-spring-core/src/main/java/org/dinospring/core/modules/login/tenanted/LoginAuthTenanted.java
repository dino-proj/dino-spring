// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.login.tenanted;

import java.io.Serializable;
import java.util.List;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.core.modules.login.LoginAuth;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginAuthTenanted<U extends User<K>, K extends Serializable> extends LoginAuth<U, K> {

  @Schema(description = "用户最近使用的租户")
  private Tenant currentTenant;

  @Schema(description = "用户所属的所有租户")
  private List<Tenant> tenantList;

}
