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

package org.dinospring.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lombok.RequiredArgsConstructor;

/**
 * 绑定字典注解
 * @author Cody LU
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BindDict {

  /***
   * 绑定数据字典Key
   * @return
   */
  String key();

  /***
   * 数据字典项取值字段
   * @return
   */
  DictFilds field() default DictFilds.VALUE;

  /**
   * 数据字典KEY的优先查询值，默认是租户级别
   * @return
   */
  Scope scope() default Scope.TENANT;

  @RequiredArgsConstructor
  public enum DictFilds {
    //item_name
    NAME("item_name"),
    //item_value
    VALUE("item_value"),
    //item_icon
    ICON("item_icon");

    private final String fieldName;

    @Override
    public String toString() {
      return fieldName;
    }
  }

  public enum Scope {
    //系统级字典
    SYSTEM,
    //租户级字典
    TENANT,
    //租户优先，租户未找到，则使用系统级别
    TENENT_PREFER
  }

}
