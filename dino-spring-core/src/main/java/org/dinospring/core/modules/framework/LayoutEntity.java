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

import java.io.Serializable;

import javax.persistence.Column;

import org.dinospring.commons.Scope;
import org.dinospring.data.domain.TenantableEntityBase;

import lombok.Data;

@Data
public class LayoutEntity<T extends Serializable> extends TenantableEntityBase<Long> {
  private String title;

  @Column(name = "access_scope", columnDefinition = "json", nullable = true)
  private Scope accessScope;

  @Column(name = "config", columnDefinition = "json", nullable = true)
  private T config;
}
