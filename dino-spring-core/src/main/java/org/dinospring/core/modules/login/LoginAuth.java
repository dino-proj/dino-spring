// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.login;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.dinospring.commons.sys.User;
import org.dinospring.core.sys.token.Token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author Cody Lu
 */

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginAuth<U extends User<K>, K extends Serializable> {

  @Schema(description = "用户信息")
  private U user;

  @Schema(description = "用户的登录凭证")
  private Token authToken;
}
