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

package org.dinospring.data.schema;

import org.springframework.core.ResolvableType;

import lombok.Data;

/**
 *
 * @author Cody LU
 * @date 2022-08-19 05:00:55
 */

@Data
public class FieldDef {
  private String name;

  private ResolvableType javaType;

  private ResolvableType javaClass;

  private String typeDefinition;

  private int size;

  private int precision;

  private int scale;

  private boolean nullable;

  private boolean unique;

  private boolean primaryKey;

  private boolean autoIncrement;

  private boolean identity;

  private boolean updateable;

  private boolean generated;

  private String defaultValue;

  private String constraint;

  private String comment;

}
