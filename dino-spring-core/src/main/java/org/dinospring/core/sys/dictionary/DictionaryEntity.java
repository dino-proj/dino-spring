// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
 * @author Cody Lu
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
