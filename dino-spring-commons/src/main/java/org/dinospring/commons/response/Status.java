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

package org.dinospring.commons.response;

import javax.annotation.Nonnull;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.Assert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

/**
 * 响应状态码及msg
 * @author tuuboo
 */

public interface Status {

  @RequiredArgsConstructor
  @Schema(name = "CODE", title = "响应的状态码")
  enum CODE implements Status {
    //成功
    OK(0, "Success"),
    //操作失败
    ERROR(1, "操作失败"),

    //登录失败
    FAIL_EXISTS(10, "已存在"),

    //登录失败
    FAIL_LOGIN(600, "登录失败"),

    //账号不允许登录
    FAIL_LOGIN_DENNY(601, "账号不允许登录"),

    //账号或密码错误
    FAIL_INVALID_PASSWORD(610, "账号或密码错误"),

    //手机号错误
    FAIL_INVALID_PHONE(611, "手机号错误"),

    //验证码不正确
    FAIL_INVALID_CAPTCHA(612, "验证码不正确"),

    //登录token已过期
    FAIL_INVALID_AUTH_TOKEN(630, "token不正确"),

    //已经在另一台设备上登录了
    FAIL_TOKEN_LOGIN_ANOTHER_DEVICE(631, "已经在另一台设备上登录了"),

    //refresh token已经过期
    FAIL_INVALID_REFRESH_TOKEN(640, "refresh token已经过期"),

    //部分成功
    WARN_PARTIAL_SUCCESS(1001, "部分成功"),

    //潜在的性能问题
    WARN_PERFORMANCE_ISSUE(1002, "潜在的性能问题"),

    //请求参数错误
    FAIL_INVALID_PARAM(4000, "请求参数错误"),

    //无权执行该操作
    FAIL_NO_PERMISSION(4003, "无权执行该操作"),

    //资源不存在
    FAIL_NOT_FOUND(4004, "资源不存在"),

    //数据校验不通过
    FAIL_VALIDATION(4005, "数据校验不通过"),

    //操作执行失败
    FAIL_OPERATION(4006, "操作执行失败"),

    //请求连接超时
    FAIL_REQUEST_TIMEOUT(4008, "请求连接超时"),

    //租户不存在
    FAIL_TENANT_NOT_EXIST(4041, "租户不存在"),

    //用户不存在
    FAIL_USER_NOT_EXIST(4042, "用户不存在"),

    //系统异常
    FAIL_EXCEPTION(5000, "系统异常"),

    //持久化失败
    FAIL_PERSISTENT_EXCEPTION(5001, "持久化失败"),

    //查询失败
    FAIL_QUERY_EXCEPTION(5002, "查询失败"),

    //服务不可用
    FAIL_SERVICE_UNAVAILABLE(5003, "服务不可用"),

    //系统IO异常
    FAIL_IO_EXCEPTION(5004, "系统IO异常"),

    //缓存清空
    CACHE_EMPTY(9999, "缓存清空");

    private final int iCode;
    private final String msg;

    @Override
    public int getCode() {
      return this.iCode;
    }

    @Override
    public String getMsg() {
      return msg;
    }

    @Override
    public Status withMsg(String msg) {
      return Status.fail(this.iCode, msg);
    }

    @Override
    public Status withMsg(String msg, Object... args) {
      return Status.fail(this.iCode, msg, args);
    }
  }

  /**
   * 获取状态码
   * @return
   */
  int getCode();

  /**
   * 获取错误消息
   * @return
   */
  String getMsg();

  /**
   * 替换消息
   * @param msg 消息信息
   * @return
   */
  Status withMsg(String msg);

  /**
   * 替换消息信息
   * @param msg 消息模板，'{}'为参数占位符，例如：{@code "user {} not found"}
   * @param args 消息模板的参数
   * @return
   */
  Status withMsg(String msg, Object... args);

  /**
   * 生成成功Status
   * @return
   */
  static Status ok() {
    return CODE.OK;
  }

  /**
   * 生成成功Status
   * @param msg 错误消息
   * @return
   */
  static Status ok(@Nonnull String msg) {
    return new DefaultStatus(0, msg);
  }

  /**
   * 生成失败Status，默认为 {@link org.dinospring.commons.response.Status.CODE.ERROR}
   * @param msg 错误消息
   * @return
   */
  static Status fail(@Nonnull String msg) {
    return fail(CODE.ERROR.getCode(), msg);
  }

  /**
   * 生成失败Status，默认为 {@link org.dinospring.commons.response.Status.CODE.ERROR}
   *  如：{@code fail("参数{}不能为空", "name")}
   * @param msg 错误消息，用{@link org.slf4j.helpers.MessageFormatter} 进行格式化
   * @param args 消息格式化参数
   * @return
   */
  static Status fail(@Nonnull String msg, Object... args) {
    return fail(CODE.ERROR.getCode(), MessageFormatter.arrayFormat(msg, args).getMessage());
  }

  /**
   * 生成失败Status
   * @param code 状态码
   * @param msg 错误消息
   * @return
   */
  static Status fail(int code, @Nonnull String msg) {
    Assert.isTrue(code > 0, "code must great than 0");
    return new DefaultStatus(code, msg);
  }

  /**
   * 生成失败Status, 并对错误消息进行参数化，如：{@code fail(-1, "参数{}不能为空", "name")}
   * @param code 状态码
   * @param msg 错误消息，用{@link org.slf4j.helpers.MessageFormatter} 进行格式化
   * @param args 消息格式化参数
   * @return
   */
  static Status fail(int code, @Nonnull String msg, Object... args) {
    Assert.isTrue(code > 0, "code must great than 0");
    return new DefaultStatus(code, MessageFormatter.arrayFormat(msg, args).getMessage());
  }

  /**
   * 生成参数错误Status
   * @param msg 错误消息
   * @return
   */
  static Status invalidParam(@Nonnull String msg) {
    return fail(CODE.FAIL_INVALID_PARAM.getCode(), msg);
  }

  /**
   * 生成失败Status, 并对错误消息进行参数化，如：{@code invalidParam(-1, "参数{}不能为空", "name")}
   * @param msg 错误消息，用{@link org.slf4j.helpers.MessageFormatter} 进行格式化
   * @param args 消息格式化参数
   * @return
   */
  static Status invalidParam(@Nonnull String msg, Object... args) {
    return fail(CODE.FAIL_INVALID_PARAM.getCode(), MessageFormatter.arrayFormat(msg, args).getMessage());
  }

}

@RequiredArgsConstructor
class DefaultStatus implements Status {

  private final int code;
  private final String msg;

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  @Override
  public Status withMsg(String msg) {
    return Status.fail(this.code, msg);
  }

  @Override
  public Status withMsg(String msg, Object... args) {
    return Status.fail(this.code, msg, args);
  }

}
