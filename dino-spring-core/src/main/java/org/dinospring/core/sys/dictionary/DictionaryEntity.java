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

package org.dinospring.core.sys.dictionary;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 数据字典实体
 * @author Cody LU
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@Entity
@Table(name = "sys_dictionary", indexes = @Index(name = "idx_sys_dictionary__tenant_key", columnList = "tenant_id,key"))
public class DictionaryEntity extends TenantRowEntityBase<Long> {

  @Schema(description = "数据字典键值")
  @NotNull(message = "数据字典键值不能为空！")
  @Size(max = 50, message = "数据字典键值长度超长！")
  @Column(name = "key", length = 50)
  private String key;

  @Schema(description = "备注")
  @Size(max = 200, message = "数据字典备注长度超长！")
  private String description;

  @Schema(description = "是否为系统预置（预置不可删除）")
  private boolean deletable = false;

  @Schema(description = "是否可编辑")
  private boolean editable = false;

}
