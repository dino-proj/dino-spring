// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.config;

import cn.dinodev.spring.core.annotion.SchemaJson;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
 */

public interface Property {

  /**
   * ID
   * @return
   */
  @Schema(description = "ID")
  Long getId();

  /**
   * scope
   * @return
   */
  @Schema(description = "scope")
  String getScope();

  /**
   * property key
   * @return
   */
  @Schema(description = "property key")
  String getKey();

  /**
   * property value
   * @return
   */
  @SchemaJson
  Object getValue();
}
