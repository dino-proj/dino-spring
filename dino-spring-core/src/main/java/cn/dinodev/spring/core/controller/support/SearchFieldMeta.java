// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller.support;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JL
 * @Date: 2021/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFieldMeta<M extends FieldEnum> implements Serializable {

  @Schema(description = "数据库字段名称")
  @Parameter(name = "field", description = "数据库字段名称")
  private List<M> field;

  @Schema(description = "关键字")
  @Parameter(name = "keyword", description = "关键字")
  private String keyword;

}
