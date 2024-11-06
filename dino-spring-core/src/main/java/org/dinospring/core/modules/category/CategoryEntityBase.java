// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.category;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.dinospring.data.domain.EntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@NoArgsConstructor
@FieldNameConstants
public abstract class CategoryEntityBase extends EntityBase<Long> {
  @Schema(description = "分类名称", maxLength = 100)
  @Column(nullable = false, length = 200)
  private String name;

  @Schema(description = "分类图标", maxLength = 100)
  @Column(nullable = true, length = 200)
  private String icon;

  @Schema(description = "父分类ID，0代表根节点")
  @Column(name = "parent_id", nullable = false)
  private Long parentId;
}
