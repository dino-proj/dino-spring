// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.request;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

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

  /**
   * 生成分页请求对象。
   *
   * <p>
   * 示例:
   * <pre>
   * PageReq pageReq = new PageReq(0, 10);
   * Pageable pageable = pageReq.pageable();
   * // 生成的分页请求对象将会请求第0页，每页10条记录
   * </pre>
   * </p>
   *
   * @return 分页请求对象
   */
  public Pageable pageable() {
    return PageRequest.of(pn, pl);
  }

  /**
   * 根据排序请求生成分页请求对象。
   *
   * <p>
   * 示例:
   * <pre>
   * SortReq sortReq = new SortReq(List.of("name:desc"));
   * PageReq pageReq = new PageReq(0, 10);
   * Pageable pageable = pageReq.pageable(sortReq);
   * // 生成的分页请求对象将会请求第0页，每页10条记录，并按照name降序排序
   * </pre>
   * </p>
   *
   * @param sortReq 排序请求对象
   * @return 分页请求对象
   */
  public Pageable pageable(SortReq sortReq) {
    return PageRequest.of(pn, pl, sortReq == null ? Sort.unsorted() : sortReq.sortable());
  }

  /**
   * 根据排序请求和前缀生成分页请求对象。
   *
   * <p>
   * 示例:
   * <pre>
   * SortReq sortReq = new SortReq(List.of("name:desc"));
   * PageReq pageReq = new PageReq(0, 10);
   * Pageable pageable = pageReq.pageable(sortReq, "user_");
   * // 生成的分页请求对象将会请求第0页，每页10条记录，并按照user_name降序排序
   * </pre>
   * </p>
   *
   * @param sortReq 排序请求对象
   * @param prefix 排序属性的前缀
   * @return 分页请求对象
   */
  public Pageable pageable(SortReq sortReq, String prefix) {
    Sort sort = Sort.unsorted();
    if (sortReq != null) {
      sort = sortReq.sortable(prefix);
    }
    return PageRequest.of(pn, pl, sort);
  }

  /**
   * 根据排序对象生成分页请求对象。
   *
   * <p>
   * 示例:
   * <pre>
   * Sort sort = Sort.by(Sort.Order.desc("name"));
   * PageReq pageReq = new PageReq(0, 10);
   * Pageable pageable = pageReq.pageable(sort);
   * // 生成的分页请求对象将会请求第0页，每页10条记录，并按照name降序排序
   * </pre>
   * </p>
   *
   * @param sort 排序对象
   * @return 分页请求对象
   */
  public Pageable pageable(@NonNull Sort sort) {
    return PageRequest.of(pn, pl, sort);
  }
}
