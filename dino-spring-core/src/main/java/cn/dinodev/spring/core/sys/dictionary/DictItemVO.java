// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.core.sys.dictionary;

import java.io.Serializable;

import cn.dinodev.spring.commons.Orderable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
 * @date 2023-08-10 05:28:45
 */

@Data
public class DictItemVO implements Orderable, Serializable {

  @Schema(description = "字典键值", maxLength = 50)
  private String key;

  @Schema(description = "数据字典项的键值（编码）", maxLength = 100)
  private String itemCode;

  @Schema(description = "数据字典项的显示名称", maxLength = 100)
  private String itemLabel;

  @Schema(description = "数据字典项的图标", maxLength = 100)
  private String itemIcon;

  @Schema(description = "排序号")
  private Integer orderNum;
}