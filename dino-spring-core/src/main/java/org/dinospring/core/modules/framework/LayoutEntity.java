/*
 *  Copyright 2021 dinospring.cn
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

package org.dinospring.core.modules.framework;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.dinospring.commons.VisualScope;
import org.dinospring.data.domain.TenantableEntityBase;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 页面布局
 * @author tuuboo
 */

@Data
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@Entity
@Table(name = "sys_frame_layout")
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
public class LayoutEntity<T extends LayoutConfig> extends TenantableEntityBase<Long> {
  @Schema(description = "布局标题")
  private String title;

  @Schema(description = "布局的可见范围")
  @Column(name = "access_scope", columnDefinition = "jsonb")
  @Type(type = "json")
  private VisualScope accessScope;

  @Schema(description = "布局配置")
  @Column(name = "config", columnDefinition = "jsonb")
  @Type(type = "json")
  private T config;
}
