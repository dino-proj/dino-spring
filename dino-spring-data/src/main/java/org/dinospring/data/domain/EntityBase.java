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

package org.dinospring.data.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 * Entity基础父类
 * @author tuuboo
 * @author JL
 */

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
public abstract class EntityBase<K extends Serializable> implements Serializable {

  /**
   * 默认主键字段id，类型为Long型自增，转json时转换为String
   */
  @Id
  @Schema(description = "ID")
  @GeneratedValue
  @Column(name = "id", unique = true)
  private K id;

  /**
   * 默认逻辑删除标记，is_deleted=0有效
   */
  @Schema(description = "ok-正常， deleted-删除")
  @Column(name = "status", nullable = false)
  private String status = "ok";

  /**
   * 默认记录创建时间字段，新建时由数据库赋值
   */
  @Schema(description = "创建时间")
  @CreatedDate
  @Column(name = "create_at", updatable = false, nullable = false)
  private Date createAt;

  @CreatedBy
  @Schema(description = "创建者用户ID")
  @Column(name = "create_by", updatable = false, length = 32, nullable = true)
  private String createBy;

  @LastModifiedDate
  @Schema(description = "最后更新时间")
  @Column(name = "update_at", updatable = true, nullable = false)
  private Date updateAt;

  @LastModifiedBy
  @Schema(description = "最后更新的用户ID")
  @Column(name = "update_by", updatable = true, length = 32, nullable = true)
  private String updateBy;
}
