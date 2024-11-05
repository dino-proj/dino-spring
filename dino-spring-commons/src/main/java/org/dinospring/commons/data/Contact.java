// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.data;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody LU
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Contact implements Serializable {

  @Schema(description = "联系人名字")
  private String name;

  @Schema(description = "联系人电话")
  private String phone;
}
