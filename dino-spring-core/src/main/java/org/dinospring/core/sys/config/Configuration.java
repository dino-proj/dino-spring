// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.config;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.dinospring.commons.Scope;
import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody Lu
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@MappedSuperclass
public class Configuration extends TenantRowEntityBase<Long> {

  @Column(name = "scope", nullable = false)
  private Scope scope;

  @Column(name = "scope_value")
  private String scopeValue;

  @Column(name = "conf_key", nullable = false, length = 1024)
  private String key;

  @Column(name = "conf_value", columnDefinition = "json NOT NULL")
  @Schema(implementation = Object.class, type = "json", description = "可以是原始类型，比如数字、字符串、布尔等，也可以是数组、json对象")
  private Object value;

}
