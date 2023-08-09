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

package org.dinospring.commons.response;

import java.util.Collection;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * restApi分页响应
 * @author Cody LU
 */

@Schema(description = "restApi分页响应")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageResponse<T> extends Response<Collection<? extends T>> {

  @Schema(description = "数据总条数")
  private Long total;

  @Schema(description = "本次请求返回数据条数")
  private Integer count;

  @Schema(description = "本次查询页码：从0开始", example = "0")
  private Integer pn;

  @Schema(description = "每页数据条数")
  private Integer pl;

  protected PageResponse(int code, String msg) {
    super(code, msg);
    this.total = 0L;
    this.count = 0;
  }

  protected PageResponse(int pn, int pl) {
    this.total = 0L;
    this.count = 0;
    this.pn = pn;
    this.pl = pl;
  }

  protected PageResponse(Collection<? extends T> list, Long total, Integer pn, Integer pl) {
    this.total = total;
    this.pn = pn;
    this.pl = pl;
    this.setData(list);
  }

  public PageResponse<T> set(Collection<? extends T> list, Long total) {
    this.setData(list);
    this.setTotal(total);
    return this;
  }

  @Override
  public Response<Collection<? extends T>> setData(Collection<? extends T> list) {
    this.count = list == null ? 0 : list.size();
    return super.setData(list);
  }

  @Schema(description = "数据总页数")
  public Long getTotalPage() {
    if (null != total && null != pl && pl > 0) {
      return (total + pl - 1) / pl;
    }
    return 0L;
  }

  public static <T> PageResponse<T> success(Page<T> page) {
    return new PageResponse<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
  }

  public static <T, S> PageResponse<T> success(Page<S> page,
      Function<Collection<S>, Collection<? extends T>> converter) {
    return new PageResponse<>(converter.apply(page.getContent()), page.getTotalElements(),
        page.getPageable().getPageNumber(), page.getPageable().getPageSize());
  }

  public static <T> PageResponse<T> success(int pn, int pl) {
    return new PageResponse<>(pn, pl);
  }

  public static <T> PageResponse<T> success(int pn, int pl, Collection<T> list, Long totalCount) {
    return new PageResponse<>(list, totalCount, pn, pl);
  }

  public static <T> PageResponse<T> success(Pageable page, Collection<T> list, Long totalCount) {
    return new PageResponse<>(list, totalCount, page.getPageNumber(), page.getPageSize());
  }

  public static <T> PageResponse<T> failPage(String msg) {
    return new PageResponse<>(Status.CODE.ERROR.getCode(), msg);
  }

  public static <T> PageResponse<T> failPage(Status status) {
    return new PageResponse<>(status.getCode(), status.getMsg());
  }
}
