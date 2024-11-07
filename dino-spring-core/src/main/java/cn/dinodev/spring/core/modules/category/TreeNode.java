// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.category;

import cn.dinodev.spring.commons.data.Option;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TreeNode extends Option<Long> {
  @Schema(description = "子节点个数")
  private Integer childCount;
}
