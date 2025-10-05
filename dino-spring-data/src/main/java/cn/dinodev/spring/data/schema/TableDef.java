// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.schema;

import java.util.List;

import org.springframework.core.ResolvableType;

import lombok.Data;

/**
 * 数据库表定义
 * 用于描述数据库表的结构信息，包括表名、字段、索引等元数据
 *
 * @author Cody Lu
 * @date 2022-08-19 05:00:55
 */

@Data
public class TableDef {

  private String name;

  private ResolvableType javaClass;

  private String databaseSchema;

  private List<FieldDef> fields;

  private List<IndexDef> indexes;

  private List<UniqueConstraint> uniqueConstraints;

  private String comment;
}
