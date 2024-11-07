// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.schema;

import java.util.List;

import lombok.Data;

/**
 *
 * @author Cody Lu
 * @date 2022-08-19 05:02:49
 */

@Data
public class UniqueConstraint {
  private String name;
  private List<String> fields;
}
