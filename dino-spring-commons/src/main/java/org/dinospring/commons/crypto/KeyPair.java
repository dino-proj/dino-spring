// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 * @date 2022-05-06 06:40:57
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPair {

  private byte[] privateKey;
  private byte[] publicKey;
}
