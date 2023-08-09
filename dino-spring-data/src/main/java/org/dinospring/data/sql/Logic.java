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

package org.dinospring.data.sql;

/**
 *
 * @author Cody LU
 * @date 2022-03-07 19:12:58
 */

public enum Logic {
  /**
   * AND
   */
  AND("AND"),
  /**
   * OR
   */
  OR("OR");

  private final String logic;

  /**
   *
   */
  private Logic(String logic) {
    this.logic = logic;
  }

  public String getLogic() {
    return logic;
  }

  @Override
  public String toString() {
    return logic;
  }
}
