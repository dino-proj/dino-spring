// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 密钥对类，用于存储公钥和私钥信息
 * 
 * @author Cody Lu
 * @since 2022-05-06
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPair {

  private byte[] privateKey;
  private byte[] publicKey;
}
