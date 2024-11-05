// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.domain;

import java.util.Base64;

/**
 *
 * @author Cody LU
 * @author JL
 */

public interface IdService {

  /**
   * 生成一个Long的全局唯一的Id
   * @return
   */
  Long genId();

  /**
   * 生层一个字符串类型的Id
   * @return
   */
  default String genIdStr() {
    long id = genId();
    id <<= 3;
    return Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(id).getBytes());
  }

  /**
   * 生成一个带前缀的ID
   * @param prefix 前缀
   * @return
   */
  default String genIdStr(String prefix) {
    return prefix + genIdStr();
  }

  /**
   * 生成一个UUID
   * @return
   */
  String genUUID();

}
