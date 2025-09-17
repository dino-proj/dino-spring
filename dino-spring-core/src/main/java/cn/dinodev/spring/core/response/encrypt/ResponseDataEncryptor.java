// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.response.encrypt;

/**
 * 响应数据加密器接口, 用于加密响应数据
 * @author Cody Lu
 * @date 2025-09-17 18:47:28
 */

public interface ResponseDataEncryptor {

  /**
   * 加密数据
   *
   * @param plainText 明文数据
   * @return 加密后的数据
   */
  String encryptData(String plainText);

}
