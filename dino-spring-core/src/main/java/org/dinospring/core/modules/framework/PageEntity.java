// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.framework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author tuuboo
 * @author JL
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@Entity
@Table(name = "sys_frame_page")
public class PageEntity extends TenantRowEntityBase<Long> {

  @Schema(title = "页面标题")
  @Column(name = "title", length = 64)
  private String title;

  @Schema(title = "页面对应的模板")
  @Column(name = "template_name", length = 64)
  private String templateName;

  @Schema(title = "页面的配置属性", type = "json")
  @Column(name = "config", columnDefinition = "jsonb")
  private PageConfig config;

}
