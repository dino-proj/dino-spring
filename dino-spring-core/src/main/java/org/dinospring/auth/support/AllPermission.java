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

package org.dinospring.auth.support;

import org.dinospring.auth.Permission;

/**
 * 所有权限，总是有权限访问
 * @author tuuboo
 * @date 2022-04-07 03:26:06
 */

public class AllPermission implements Permission {
  private static final AllPermission INSTANCE = new AllPermission();

  @Override
  public boolean implies(Permission permission) {
    return true;
  }

  public static AllPermission of() {
    return INSTANCE;
  }

}
