// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.schema;

import java.util.List;

import lombok.Data;

/**
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
