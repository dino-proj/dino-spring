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

package org.dinospring.core.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.dinospring.data.domain.EntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author tuuboo
 */

@Data
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@MappedSuperclass
public abstract class AbstractOrg extends EntityBase<Long> {

  /**
   * 组织树根节点ID
   */
  public static final Long ROOT_ID = 0L;

  @Schema(description = "父节点ID")
  @Column(name = "parent_id", nullable = false)
  private Long parentId;

  @Schema(description = "节点名字")
  @Column(length = 100, nullable = false)
  private String name;

  @Schema(description = "节点深度")
  @Column(nullable = false)
  private Integer depth;

  @Schema(description = "节点ID级联，最后一个为本节点ID")
  @Column(name = "cascade_ids", nullable = false, columnDefinition = "jsonb")
  private Long[] cascadeIds;

  @Schema(description = "节点Name级联，最后一个为本节点Name")
  @Column(name = "cascade_names", nullable = false, columnDefinition = "jsonb")
  private String[] cascadeNames;

}
