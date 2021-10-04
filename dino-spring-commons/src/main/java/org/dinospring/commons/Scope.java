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

package org.dinospring.commons;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

/**
 * scope 接口，用于定义scope范围
 * @author tuuboo
 */
public interface Scope extends Serializable {

  /**
   * scope 的名字
   * @return
   */
  String getName();

  /**
   * scope 顺序
   * @return
   */
  int getOrder();

  /**
   * 比自己优先级低的Scopes
   * @param includeThis 是否包含本Scope
   * @return 比自己小或等于自己排序的Scopes
   */
  Scope[] lowerScopes(boolean includeThis);

  /**
   * 比自己优先级高的Scopes
   * @param includeThis 是否包含本Scope
   * @return 比自己大或等于自己排序的Scopes
   */
  Scope[] higherScopes(boolean includeThis);

  @AllArgsConstructor
  enum DEFAULT implements Scope {
    //系统级
    SYS("sys", 0),
    //租户级
    TENANT("tenant", 1),
    //页面级
    PAGE("page", 2),
    //用户级
    USER("user", 3);

    private static final Scope[] EMPTY_ARRAY = new Scope[0];

    private String name;
    private int order;

    @Override
    public String getName() {
      return name;
    }

    @Override
    public int getOrder() {
      return order;
    }

    @Override
    public Scope[] lowerScopes(boolean includeThis) {
      return Arrays.stream(DEFAULT.values())
          .filter(s -> s.getOrder() < this.getOrder() || (includeThis && s.getOrder() == this.getOrder()))
          .collect(Collectors.toList()).toArray(EMPTY_ARRAY);
    }

    @Override
    public Scope[] higherScopes(boolean includeThis) {
      return Arrays.stream(DEFAULT.values())
          .filter(s -> s.getOrder() > this.getOrder() || (includeThis && s.getOrder() == this.getOrder()))
          .collect(Collectors.toList()).toArray(EMPTY_ARRAY);
    }

    @Override
    public String toString() {
      return name;
    }

    public static Function<String, Scope> provider() {
      return DEFAULT::of;
    }

    public static Scope of(String name) {
      return DEFAULT.valueOf(name.toUpperCase());
    }

  }
}
