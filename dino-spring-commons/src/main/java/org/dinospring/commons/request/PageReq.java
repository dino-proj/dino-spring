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

package org.dinospring.commons.request;

import javax.validation.constraints.Min;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求信息
 * @author tuuboo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class PageReq {

  /**
   * The Page.
   */
  @Min(0)
  @Parameter(description = "页码，从0开始 (0..N)", schema = @Schema(type = "integer", defaultValue = "0"))
  private Integer pn = 0;

  /**
   * The Size.
   */
  @Min(1)
  @Parameter(description = "页长，最小为1", schema = @Schema(type = "integer", defaultValue = "10"))
  private Integer pl = 10;

  public Pageable pageable() {
    return PageRequest.of(pn, pl);
  }

  public Pageable pageable(SortReq sortReq) {
    return PageRequest.of(pn, pl, sortReq == null ? Sort.unsorted() : sortReq.sortable());
  }

  public Pageable pageable(SortReq sortReq, String prefix) {
    Sort sort = null;
    if (sortReq != null) {
      sort = sortReq.sortable(prefix);
    }
    return PageRequest.of(pn, pl, sort);
  }

}
