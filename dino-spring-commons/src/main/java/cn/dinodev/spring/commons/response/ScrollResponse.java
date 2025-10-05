// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.response;

import java.util.Collection;
import java.util.function.Function;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * REST API滑动窗口响应类，用于分页滚动加载
 *
 * @param <T> 响应数据元素类型
 * @author Cody Lu
 * @since 2022-07-11
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

  /**
   * 构造函数，用于创建失败的响应
   * @param code 状态码
   * @param msg 消息
   */
  protected ScrollResponse(int code, String msg) {
    super(code, msg);
    this.count = 0;
    this.hasMore = null;
  }

  /**
   * 构造函数，用于创建成功的响应
   * @param list 数据列表
   * @param nextCursor 下一个游标
   * @param hasMore 是否有更多数据
   */
  protected ScrollResponse(Collection<? extends T> list, @Nullable String nextCursor, boolean hasMore) {
    super.setData(list);
    this.count = list == null ? 0 : list.size();
    this.cursor = nextCursor;
    this.hasMore = hasMore;
  }

  /**
   * 设置滚动响应的数据
   * @param list 数据列表
   * @param nextCursor 下一个游标
   * @param hasMore 是否有更多数据
   * @return 当前 {@link ScrollResponse} 实例
   */
  public ScrollResponse<T> set(Collection<? extends T> list, @Nullable String nextCursor, boolean hasMore) {
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

  /**
   * 创建一个成功的滚动响应
   * @param list 数据列表
   * @param nextCursor 下一个滚动ID
   * @param hasMore 是否有更多数据
   * @param <T> 数据类型
   * @return 成功的滚动响应
   */
  public static <T> ScrollResponse<T> success(@Nonnull Collection<? extends T> list, @Nullable String nextCursor,
      boolean hasMore) {
    return new ScrollResponse<>(list, nextCursor, hasMore);
  }

  /**
   * 创建一个成功的滚动响应，并对数据进行转换
   * @param list 原始数据列表
   * @param converter 数据转换函数
   * @param nextCursor 下一个游标
   * @param hasMore 是否有更多数据
   * @param <T> 目标数据类型
   * @param <S> 原始数据类型
   * @return 转换后的成功滚动响应
   */
  public static <T, S> ScrollResponse<T> success(@Nonnull Collection<S> list,
      @Nonnull Function<Collection<S>, Collection<? extends T>> converter, @Nullable String nextCursor,
      boolean hasMore) {
    return new ScrollResponse<>(converter.apply(list), nextCursor, hasMore);
  }

  /**
   * 创建一个表示有更多数据的成功滚动响应
   * @param list 数据列表
   * @param nextCursor 下一个游标
   * @param <T> 数据类型
   * @return 成功的滚动响应
   */
  public static <T> ScrollResponse<T> successWithMore(@Nonnull Collection<? extends T> list,
      @Nonnull String nextCursor) {
    return new ScrollResponse<>(list, nextCursor, true);
  }

  /**
   * 创建一个表示没有更多数据的成功滚动响应
   * @param list 数据列表
   * @param <T> 数据类型
   * @return 成功的滚动响应
   */
  public static <T> ScrollResponse<T> successWithNoMore(@Nonnull Collection<? extends T> list) {
    return new ScrollResponse<>(list, null, false);
  }

  /**
   * 创建一个失败的分页响应
   * @param msg 失败消息
   * @param <T> 数据类型
   * @return 失败的滚动响应
   */
  public static <T> ScrollResponse<T> failPage(String msg) {
    return new ScrollResponse<>(Status.CODE.ERROR.getCode(), msg);
  }

  /**
   * 创建一个失败的分页响应
   * @param status 状态
   * @param <T> 数据类型
   * @return 失败的滚动响应
   */
  public static <T> ScrollResponse<T> failPage(Status status) {
    return new ScrollResponse<>(status.getCode(), status.getMsg());
  }
}
