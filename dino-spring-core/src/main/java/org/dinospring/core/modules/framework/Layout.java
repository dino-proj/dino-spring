// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework;

import org.dinospring.commons.Scope;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

/**
 * 页面布局
 * @author Cody Lu
 */

@Data
public class Layout<T extends LayoutConfig> {

  @Schema(description = "布局Id")
  private Long id;

  @Schema(description = "布局标题")
  private String title;

  @Schema(description = "布局的可见范围")
  @Column(name = "access_scope", columnDefinition = "jsonb", nullable = true)
  private Scope accessScope;

  @Schema(description = "布局配置")
  @Column(name = "config", columnDefinition = "jsonb", nullable = true)
  private T config;
}
