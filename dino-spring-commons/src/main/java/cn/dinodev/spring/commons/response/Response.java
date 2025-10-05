// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * REST API通用响应包装类
 *
 * @param <T> 响应数据类型
 * @author Cody Lu
 */

@ApiResponse(description = "restApi响应", content = @Content(mediaType = "application/json"))
@Data
@Accessors(chain = true)
public class Response<T> {

  @Schema(description = "响应状态码:0为成功,其他码值为失败", required = true, example = "0")
  private Integer code;

  @Schema(description = "响应提示信息:成功为success,其余为对应的错误信息", example = "success")
  private String msg;

  @Schema(description = "业务响应数据")
  private T data;

  @Schema(description = "响应耗时(毫秒)", required = true, example = "50")
  private Long cost;

  /**
   * 默认构造函数，初始化为成功状态
   */
  protected Response() {
    this.code = 0;
    this.msg = "success";
  }

  /**
   * 构造函数，指定状态码和消息
   * @param code 状态码
   * @param msg 消息
   */
  protected Response(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  /**
   * 构造函数，指定状态码、消息和数据
   * @param code 状态码
   * @param msg 消息
   * @param data 数据
   */
  protected Response(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  /**
   * 构造函数，指定状态码、消息、数据和耗时
   * @param code 状态码
   * @param msg 消息
   * @param data 数据
   * @param cost 耗时
   */
  protected Response(int code, String msg, T data, Long cost) {
    this.code = code;
    this.msg = msg;
    this.data = data;
    this.cost = cost;
  }

  /**
   * 创建一个成功的响应
   * @param <T> 响应数据类型
   * @return 成功的响应
   */
  public static <T> Response<T> success() {
    return new Response<>(0, "success");
  }

  /**
   * 创建一个带数据的成功响应
   * @param data 响应数据
   * @param <T> 响应数据类型
   * @return 带数据的成功响应
   */
  public static <T> Response<T> success(T data) {
    Response<T> resp = success();
    resp.setData(data);
    return resp;
  }

  /**
   * 创建一个失败的响应
   * @param msg 失败消息
   * @param <T> 响应数据类型
   * @return 失败的响应
   */
  public static <T> Response<T> fail(String msg) {
    return new Response<>(Status.CODE.ERROR.getCode(), msg);
  }

  /**
   * 创建一个失败的响应
   * @param status 状态
   * @param <T> 响应数据类型
   * @return 失败的响应
   */
  public static <T> Response<T> fail(Status status) {
    return new Response<>(status.getCode(), status.getMsg());
  }
}
