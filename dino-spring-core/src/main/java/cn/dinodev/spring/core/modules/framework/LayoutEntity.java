// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import cn.dinodev.spring.commons.VisualScope;
import cn.dinodev.spring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 * 页面布局
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@Entity
@Table(name = "sys_frame_layout")
public class LayoutEntity extends TenantRowEntityBase<Long> {
  @Schema(description = "布局标题")
  private String title;

  @Schema(description = "布局的可见范围")
  @Column(name = "access_scope", columnDefinition = "jsonb")
  private VisualScope accessScope;

  @Schema(name = "exclude_scope", description = "布局的排除可见范围")
  @Column(name = "exclude_scope", columnDefinition = "jsonb")
  private VisualScope excludeScope;

  @Schema(description = "布局配置")
  @Column(name = "config", columnDefinition = "jsonb")
  private LayoutConfig config;
}
