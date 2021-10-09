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

package org.dinospring.core.sys.config;

import org.dinospring.core.annotion.SchemaJson;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author tuuboo
 */

public interface Property {

  /**
   * ID
   * @return
   */
  @Schema(description = "ID")
  Long getId();

  /**
   * scope
   * @return
   */
  @Schema(description = "scope")
  String getScope();

  /**
   * property key
   * @return
   */
  @Schema(description = "property key")
  String getKey();

  /**
   * property value
   * @return
   */
  @SchemaJson
  Object getValue();
}
