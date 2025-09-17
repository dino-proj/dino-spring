// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * restApi响应
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

  protected Response() {
    this.code = 0;
    this.msg = "success";
  }

  protected Response(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static <T> Response<T> success() {
    return new Response<>(0, "success");
  }

  public static <T> Response<T> success(T data) {
    Response<T> resp = success();
    resp.setData(data);
    return resp;
  }

  public static <T> Response<T> fail(String msg) {
    return new Response<>(Status.CODE.ERROR.getCode(), msg);
  }

  public static <T> Response<T> fail(Status status) {
    return new Response<>(status.getCode(), status.getMsg());
  }
}
