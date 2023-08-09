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

package org.dinospring.commons.binding;

import java.util.Collection;

/**
 * binder 接口
 * @author Cody LU
 * @date 2022-04-13 04:11:45
 */

public interface Binder<K> {

  /**
   * 绑定单个对象
   *
   * @param target
   */
  <T> void bind(T target);

  /**
   * 批量绑定一组对象
   *
   * @param targets
   */
  <T> void bindBatch(Collection<T> targets);
}
