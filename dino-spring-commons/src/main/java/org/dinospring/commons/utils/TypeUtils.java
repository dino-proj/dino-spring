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

package org.dinospring.commons.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.experimental.UtilityClass;

/**
 *
 * @author tuuboo
 */

@UtilityClass
public class TypeUtils {

  /**
   * 获取某个泛型接口的实际参数
   * @param inst 对象实例
   * @param interfaceClass 需要查询的接口类
   * @param paramIndex 类型参数的索引，从0开始
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getGenericParamClass(Object inst, Class<?> interfaceClass, int paramIndex) {
    var iClass = inst.getClass().getGenericInterfaces();
    for (Type type : iClass) {
      if (type instanceof ParameterizedType) {
        var tp = (ParameterizedType) type;
        if (tp.getRawType().equals(interfaceClass)) {
          var t = tp.getActualTypeArguments()[paramIndex];
          if (t instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) t).getRawType();
          } else {
            return (Class<T>) t;
          }
        }
      }
    }
    return null;
  }
}
