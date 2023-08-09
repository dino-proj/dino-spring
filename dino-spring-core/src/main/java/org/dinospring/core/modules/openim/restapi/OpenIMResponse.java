// Copyright 2022 dinodev.cn
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

package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * openim的restful接口返回结果
 * @author Cody LU
 * @date 2022-04-12 20:14:42
 */

@Data
public abstract class OpenIMResponse<T> {
  /**
   * 返回码
   */
  @JsonProperty("errCode")
  private Integer errCode;

  /**
   * 返回信息
   */
  @JsonProperty("errMsg")
  private String errMsg;

  /**
   * 返回数据
   */
  @JsonProperty("data")
  private T data;

}
