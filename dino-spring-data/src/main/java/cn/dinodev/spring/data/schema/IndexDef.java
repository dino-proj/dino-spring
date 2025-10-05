// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.schema;

import java.util.List;

import lombok.Data;

/**
 * 数据库索引定义
 * 用于描述数据库表索引的结构信息，包括索引名、字段列表、索引类型等
 *
 * @author Cody Lu
 * @date 2022-08-19 05:00:55
 */

@Data
public class IndexDef {

  private String name;

  private List<String> fields;

  private boolean unique;
}
