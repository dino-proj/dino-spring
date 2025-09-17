// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.dictionary;

import java.util.List;

import cn.dinodev.spring.core.vo.VoImplBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryVO extends VoImplBase<Long> {

  @Schema(description = "数据字典键值", maxLength = 50)
  private String key;

  @Schema(description = "数据字典项")
  private List<DictItemVO> items;

  @Schema(description = "备注", maxLength = 200)
  private String description;

  @Schema(description = "是否为系统预置（预置不可删除）")
  private Boolean deletable;

  @Schema(description = "是否可编辑")
  private Boolean editable;

}
