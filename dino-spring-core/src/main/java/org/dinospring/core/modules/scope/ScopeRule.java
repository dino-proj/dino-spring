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

package org.dinospring.core.modules.scope;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Cody LU
 * @date 2022-03-31 16:50:48
 */

public interface ScopeRule extends Serializable {

  /**
   * 转为Json串
   * @param objectMapper
   * @return
   */
  String toJson(ObjectMapper objectMapper);

  /**
   * 计算规则的Hash值
   * @return
   */
  String hash();
}
