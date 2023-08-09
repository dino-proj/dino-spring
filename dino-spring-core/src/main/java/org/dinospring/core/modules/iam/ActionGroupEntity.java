// Copyright 2022 dinodev.cn
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

package org.dinospring.core.modules.iam;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.dinospring.data.domain.EntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "iam_action_group")
public class ActionGroupEntity extends EntityBase<Long> {

  @Schema(name = "user_type", description = "用户类型，null表示适用所有用户", maxLength = 64)
  @Column(name = "user_type", nullable = true, length = 64)
  private String userType;

  @Schema(description = "权限组的名字")
  @Column(length = 64)
  private String name;

  @Schema(description = "权限组的描述")
  @Column(length = 128)
  private String remark;

  @Convert(disableConversion = true)
  @Schema(description = "包含的权限")
  @Column(columnDefinition = "jsonb")
  private List<Action> actions;
}
