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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author tuuboo
 * @date 2022-04-12 13:10:43
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "iam_resource", indexes = {
    @Index(name = "idx_iam_resource_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_iam_resource_code", columnList = "code")
})
public class PermissionEntity extends TenantRowEntityBase<Long> {
  @Column(name = "code", length = 64, nullable = false, unique = true)
  private String code;

  @Column(name = "title", length = 64, nullable = false)
  private String title;

  @Column(name = "remark", length = 255)
  private String remark;

  @Convert(disableConversion = true)
  @Column(name = "actions", columnDefinition = "jsonb")
  private List<Action> actions;
}
