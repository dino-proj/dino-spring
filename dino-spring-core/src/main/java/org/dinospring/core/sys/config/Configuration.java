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

package org.dinospring.core.sys.config;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.dinospring.commons.Scope;
import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@MappedSuperclass
public class Configuration extends TenantRowEntityBase<Long> {

  @Column(name = "scope", nullable = false)
  private Scope scope;

  @Column(name = "scope_value")
  private String scopeValue;

  @Column(name = "conf_key", nullable = false, length = 1024)
  private String key;

  @Column(name = "conf_value", columnDefinition = "json NOT NULL")
  @Schema(implementation = Object.class, type = "json", description = "可以是原始类型，比如数字、字符串、布尔等，也可以是数组、json对象")
  private Object value;

}
