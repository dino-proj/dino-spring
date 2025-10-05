// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.response;

import java.util.Collection;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * REST API分页响应包装类
 *
 * @param <T> 分页数据元素类型
 * @author Cody Lu
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

  /**
   * 构造函数，用于创建失败的响应
   * @param code 状态码
   * @param msg 消息
   */
  protected PageResponse(int code, String msg) {
    super(code, msg);
    this.total = 0L;
    this.count = 0;
  }

  /**
   * 构造函数，用于创建空的分页响应
   * @param pn 页码
   * @param pl 每页数量
   */
  protected PageResponse(int pn, int pl) {
    this.total = 0L;
    this.count = 0;
    this.pn = pn;
    this.pl = pl;
  }

  /**
   * 构造函数，用于创建带数据的分页响应
   * @param list 数据列表
   * @param total 总条数
   * @param pn 页码
   * @param pl 每页数量
   */
  protected PageResponse(Collection<? extends T> list, Long total, Integer pn, Integer pl) {
    super.setData(list);
    this.count = list == null ? 0 : list.size();
    this.total = total;
    this.pn = pn;
    this.pl = pl;
  }

  /**
   * 设置分页数据和总数
   * @param list 数据列表
   * @param total 总条数
   * @return 当前 {@link PageResponse} 实例
   */
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

  /**
   * 获取总页数
   * @return 总页数
   */
  @Schema(description = "数据总页数")
  public Long getTotalPage() {
    if (null != total && null != pl && pl > 0) {
      return (total + pl - 1) / pl;
    }
    return 0L;
  }

  /**
   * 从 Spring Data Page 对象创建成功的分页响应
   * @param page Spring Data Page 对象
   * @param <T> 数据类型
   * @return 成功的分页响应
   */
  public static <T> PageResponse<T> success(Page<T> page) {
    return new PageResponse<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
  }

  /**
   * 从 Spring Data Page 对象创建成功的分页响应，并转换数据
   * @param page Spring Data Page 对象
   * @param converter 数据转换函数
   * @param <T> 目标数据类型
   * @param <S> 原始数据类型
   * @return 转换后的成功分页响应
   */
  public static <T, S> PageResponse<T> success(Page<S> page,
      Function<Collection<S>, Collection<? extends T>> converter) {
    return new PageResponse<>(converter.apply(page.getContent()), page.getTotalElements(),
        page.getPageable().getPageNumber(), page.getPageable().getPageSize());
  }

  /**
   * 创建一个空的成功分页响应
   * @param pn 页码
   * @param pl 每页数量
   * @param <T> 数据类型
   * @return 空的成功分页响应
   */
  public static <T> PageResponse<T> success(int pn, int pl) {
    return new PageResponse<>(pn, pl);
  }

  /**
   * 创建一个带数据的成功分页响应
   * @param pn 页码
   * @param pl 每页数量
   * @param list 数据列表
   * @param totalCount 总条数
   * @param <T> 数据类型
   * @return 带数据的成功分页响应
   */
  public static <T> PageResponse<T> success(int pn, int pl, Collection<T> list, Long totalCount) {
    return new PageResponse<>(list, totalCount, pn, pl);
  }

  /**
   * 从 Pageable 对象和数据列表创建成功的分页响应
   * @param page Pageable 对象
   * @param list 数据列表
   * @param totalCount 总条数
   * @param <T> 数据类型
   * @return 成功的分页响应
   */
  public static <T> PageResponse<T> success(Pageable page, Collection<T> list, Long totalCount) {
    return new PageResponse<>(list, totalCount, page.getPageNumber(), page.getPageSize());
  }

  /**
   * 创建一个失败的分页响应
   * @param msg 失败消息
   * @param <T> 数据类型
   * @return 失败的分页响应
   */
  public static <T> PageResponse<T> failPage(String msg) {
    return new PageResponse<>(Status.CODE.ERROR.getCode(), msg);
  }

  /**
   * 创建一个失败的分页响应
   * @param status 状态
   * @param <T> 数据类型
   * @return 失败的分页响应
   */
  public static <T> PageResponse<T> failPage(Status status) {
    return new PageResponse<>(status.getCode(), status.getMsg());
  }
}
