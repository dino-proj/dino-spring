// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.dinospring.core.vo.VoImplBase;

/**
 *
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class CategoryVo extends VoImplBase<Long> {
  @Schema(description = "分类名称", maxLength = 100)
  private String name;

  @Schema(description = "分类图标", maxLength = 100)
  private String icon;

  @Schema(description = "父分类ID")
  private Long parentId;

  @Schema(description = "父分类")
  private CategoryVo parentCategory;

  @Schema(description = "是否是根节点")
  public boolean isRoot() {
    return parentId == null || parentId == 0L;
  }
}
