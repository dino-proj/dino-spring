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

package org.dinospring.core.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author tuuboo
 */

@Data
@NoArgsConstructor
@SuperBuilder
@FieldNameConstants
public class VoImplBase<K extends Serializable> implements VoBase<K> {

  @Schema(description = "ID")
  private K id;

  @Schema(description = "创建时间")
  private Date createAt;

  @Schema(description = "最后更新时间")
  private Date updateAt;

  @Schema(description = "状态")
  private Integer status;
}
