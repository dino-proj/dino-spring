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

package org.dinospring.core.modules.framework;

/**
 *
 * @author tuuboo
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