// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.sql;

/**
 *
 * @author Cody Lu
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
