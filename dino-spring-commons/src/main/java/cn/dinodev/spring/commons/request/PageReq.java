// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.request;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求信息
 * @author Cody Lu
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
    Sort sort = Sort.unsorted();
    if (sortReq != null) {
      sort = sortReq.sortable(prefix);
    }
    return PageRequest.of(pn, pl, sort);
  }

}
