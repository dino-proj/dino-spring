// Copyright 2022 dinodev.cn
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

package org.dinospring.commons.function;

import java.util.function.Function;

import org.dinospring.commons.utils.TypeUtils;

/**
 *
 * @author tuuboo
 * @date 2022-06-09 03:48:25
 */

public interface Functions {
  static Function<Object, Object> IDENTITY_FUNCTION = t -> t;

  /**
   * 创建一个Function，该Function将传入的参数原样返回
   * @param <T>
   * @return
   */
  static <T> Function<T, T> identity() {
    return TypeUtils.cast(IDENTITY_FUNCTION);
  }

}
