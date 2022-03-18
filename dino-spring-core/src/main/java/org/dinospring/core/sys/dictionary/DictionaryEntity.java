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

package org.dinospring.core.sys.dictionary;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.dinospring.commons.Orderable;
import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 数据字典实体
 * @author tuuboo
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

  @Schema(description = "数据字典项")
  @OneToMany(fetch = FetchType.EAGER)
  private List<DictItem> items = new ArrayList<>();

  @Schema(description = "备注")
  @Size(max = 200, message = "数据字典备注长度超长！")
  private String description;

  @Schema(description = "是否为系统预置（预置不可删除）")
  private boolean deletable = false;

  @Schema(description = "是否可编辑")
  private boolean editable = false;

  @Data
  @EqualsAndHashCode(callSuper = true)
  @Entity(name = "DictItem")
  @Table(name = "sys_dictionary_item", indexes = @Index(name = "idx_sys_dictionary__tenant_key_itemkey", columnList = "tenant_id,key,item_key"))
  public static class DictItem extends TenantRowEntityBase<Long> implements Orderable {

    @Schema(description = "字典键值")
    @NotNull(message = "数据字典j键值不能为空！")
    @Size(max = 50, message = "数据字典键值长度超长！")
    @Column(name = "key", length = 50)
    private String key;

    @Schema(description = "数据字典项的键值（编码）")
    @NotNull(message = "数据字典项名称不能为空！")
    @Size(max = 100, message = "数据字典项键值长度不能大于100！")
    @Column(name = "item_key", length = 100)
    private String itemKey;

    @Schema(description = "数据字典项的显示名称")
    @NotNull(message = "数据字典项名称不能为空！")
    @Size(max = 100, message = "数据字典项名称长度不能大于100！")
    @Column(name = "item_name", length = 100)
    private String itemName;

    @Schema(description = "数据字典项的图标")
    @Size(max = 100, message = "字典对应的图标")
    @Column(name = "item_icon", length = 100)
    private String itemIcon;

    @Schema(description = "排序号")
    @Column(name = "order_num", nullable = true)
    private Integer orderNum;
  }
}
