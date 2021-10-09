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

package org.dinospring.core.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.dinospring.core.sys.token.TokenPrincaple;

import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data(staticConstructor = "of")
public class ShiroAuthToken implements AuthenticationToken {
  private final TokenPrincaple princ;

  private final String token;

  public ShiroAuthToken(TokenPrincaple princ, String token) {
    this.princ = princ;
    this.token = token;
  }

  @Override
  public Object getPrincipal() {
    return princ;
  }

  @Override
  public Object getCredentials() {
    return token;
  }

}