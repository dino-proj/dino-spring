// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework;

/**
 *
 * @author Cody LU
 */
public enum PageType {
  //功能类页面
  FUNCTION(1, "FUNCTION"),
  //列表类页面
  LIST(2, "LIST"),
  //自定义页面
  CUSTOM(3, "CUSTOM"),
  //详情页面
  DETAIL(4, "DETAIL"),
  ;

  private int id;
  private String type;

  private PageType(int id, String type) {
    this.id = id;
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return type;
  }
}