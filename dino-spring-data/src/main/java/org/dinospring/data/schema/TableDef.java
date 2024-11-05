// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.schema;

import java.util.List;

import org.springframework.core.ResolvableType;

import lombok.Data;

/**
 *
 * @author Cody LU
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
