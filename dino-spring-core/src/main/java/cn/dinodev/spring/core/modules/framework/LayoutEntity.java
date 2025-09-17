/*
 *  Copyright 2021 dinodev.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
