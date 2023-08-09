// Copyright 2021 dinodev.cn
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

package org.dinospring.core.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody LU
 * @author JL
 */
public interface VoBase<K extends Serializable> extends Serializable {

  /**
   * ID
   * @return id
   */
  @Schema(description = "ID")
  K getId();

  /**
   * 对象创建时间
   * @return 创建时间
   */
  @Schema(description = "创建时间")
  Date getCreateAt();

  /**
   * 对象最后更新时间
   * @return 更新时间
   */
  @Schema(description = "最后更新时间")
  Date getUpdateAt();

  /**
   * 对象状态码
   * @return
   */
  @Schema(description = "状态码")
  String getStatus();

  /**
   * 创建用户
   * @return
   */
  @Schema(description = "创建用户")
  String getCreateBy();
}
