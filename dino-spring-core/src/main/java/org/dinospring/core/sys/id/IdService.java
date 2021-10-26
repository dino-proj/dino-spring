// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.sys.id;

import java.util.Base64;

/**
 *
 * @author tuuboo
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
    return Base64.getUrlEncoder().encodeToString(String.valueOf(id).getBytes());
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
