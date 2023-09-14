// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package org.dinospring.core.sys.dictionary;

import org.dinospring.commons.Orderable;
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

/**
 *
 * @author Cody Lu
 * @date 2023-08-10 05:30:27
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_dictionary_item", indexes = @Index(name = "idx_sys_dictionary_item__tenant_key_itemcode", columnList = "tenant_id,key,item_code"))
public class DictItemEntity extends TenantRowEntityBase<Long> implements Orderable {

  @Schema(description = "字典键值")
  @NotNull(message = "数据字典键值不能为空！")
  @Size(max = 50, message = "数据字典键值长度超长！")
  @Column(name = "key", length = 50)
  private String key;

  @Schema(name = "item_code", description = "数据字典项的键值（编码）")
  @NotNull(message = "数据字典项名称不能为空！")
  @Size(max = 100, message = "数据字典项键值长度不能大于100！")
  @Column(name = "item_code", length = 100)
  private String itemCode;

  @Schema(name = "item_label", description = "数据字典项的显示名称")
  @NotNull(message = "数据字典项名称不能为空！")
  @Size(max = 100, message = "数据字典项名称长度不能大于100！")
  @Column(name = "item_label", length = 100)
  private String itemLabel;

  @Schema(name = "item_icon", description = "数据字典项的图标")
  @Size(max = 100, message = "字典对应的图标")
  @Column(name = "item_icon", length = 100)
  private String itemIcon;

  @Schema(name = "order_num", description = "排序号")
  @Column(name = "order_num", nullable = true)
  private Integer orderNum;
}