// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.schema;

import org.springframework.core.ResolvableType;

import lombok.Data;

/**
 *
 * @author Cody Lu
 * @date 2022-08-19 05:00:55
 */

@Data
public class FieldDef {
  private String name;

  private ResolvableType javaType;

  private ResolvableType javaClass;

  private String typeDefinition;

  private int size;

  private int precision;

  private int scale;

  private boolean nullable;

  private boolean unique;

  private boolean primaryKey;

  private boolean autoIncrement;

  private boolean identity;

  private boolean updateable;

  private boolean generated;

  private String defaultValue;

  private String constraint;

  private String comment;

}
