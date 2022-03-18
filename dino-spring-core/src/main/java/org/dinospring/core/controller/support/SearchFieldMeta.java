/*
 *  Copyright 2021 dinospring.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dinospring.core.controller.support;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JL
 * @Date: 2021/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFieldMeta<M extends FieldEnum> implements Serializable {

  @Schema(description = "数据库字段名称")
  @Parameter(name = "field", description = "数据库字段名称")
  private List<M> field;

  @Schema(description = "关键字")
  @Parameter(name = "keyword", description = "关键字")
  private String keyword;

}
