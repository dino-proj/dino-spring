// Copyright 2022 dinospring.cn
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

import javax.annotation.Nonnull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author tuuboo
 * @date 2022-07-11 21:21:08
 */

@Schema(description = "restApi滑动窗口响应")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ScrollResponse<T> extends Response<Collection<? extends T>> {

  @Schema(description = "是否有更多数据")
  private Boolean hasMore;

  @Schema(description = "本次请求返回数据条数")
  private Integer count;

  @Schema(description = "下次请求游标")
  private String cursor;

  protected ScrollResponse(int code, String msg) {
    super(code, msg);
    this.count = 0;
    this.hasMore = null;
  }

  protected ScrollResponse(@Nonnull Collection<? extends T> list, @Nonnull String nextCursor, boolean hasMore) {
    this.cursor = nextCursor;
    this.hasMore = hasMore;
    this.setData(list);
  }

  public ScrollResponse<T> set(Collection<? extends T> list, @Nonnull String nextCursor, boolean hasMore) {
    this.setData(list);
    this.cursor = nextCursor;
    this.hasMore = hasMore;
    return this;
  }

  @Override
  public Response<Collection<? extends T>> setData(Collection<? extends T> list) {
    this.count = list == null ? 0 : list.size();
    return super.setData(list);
  }

  public static <T> ScrollResponse<T> success(@Nonnull Collection<? extends T> list, @Nonnull String nextCursor,
      boolean hasMore) {
    return new ScrollResponse<>(list, nextCursor, hasMore);
  }

  public static <T, S> ScrollResponse<T> success(Collection<S> list,
      Function<Collection<S>, Collection<? extends T>> converter, @Nonnull String nextCursor,
      boolean hasMore) {
    return new ScrollResponse<>(converter.apply(list), nextCursor, hasMore);
  }

  public static <T> ScrollResponse<T> successWithMore(@Nonnull Collection<? extends T> list,
      @Nonnull String nextCursor) {
    return new ScrollResponse<>(list, nextCursor, true);
  }

  public static <T> ScrollResponse<T> successWithNoMore(@Nonnull Collection<? extends T> list) {
    return new ScrollResponse<>(list, null, false);
  }

  public static <T> ScrollResponse<T> failPage(String msg) {
    return new ScrollResponse<>(Status.CODE.ERROR.getCode(), msg);
  }

  public static <T> ScrollResponse<T> failPage(Status status) {
    return new ScrollResponse<>(status.getCode(), status.getMsg());
  }
}
