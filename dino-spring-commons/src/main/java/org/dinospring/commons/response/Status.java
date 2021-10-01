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

public interface Status {

    @RequiredArgsConstructor
    @Schema(name = "CODE", title = "响应的状态码")
    enum CODE implements Status {
        OK(0, "Success"), ERROR(1, "操作失败"),

        FAIL_LOGIN(600, "登录失败"),

        FAIL_LOGIN_DENNY(601, "账号不允许登录"),

        FAIL_INVALID_PASSWORD(610, "账号或密码错误"),

        FAIL_INVALID_PHONE(611, "手机号错误"),

        FAIL_INVALID_CAPTCHA(612, "验证码不正确"),

        FAIL_TOKEN_EXPIRED(630, "login token has expired"),

        FAIL_TOKEN_NOT_MATCH(631, "login token not match"),

        FAIL_REFRESH_TOKEN_EXPIRED(632, "login refresh token has expired"),

        FAIL_REFRESH_TOKEN_NOT_MATCH(633, "login refresh token not match"),

        FAIL_TOKEN_LOGIN_ANOTHER_DEVICE(634, "login token invalid and login in new device"),

        WARN_PARTIAL_SUCCESS(1001, "部分成功"),

        WARN_PERFORMANCE_ISSUE(1002, "潜在的性能问题"),

        FAIL_INVALID_PARAM(4000, "请求参数错误"),

        FAIL_NO_PERMISSION(4003, "无权执行该操作"),

        FAIL_NOT_FOUND(4004, "资源不存在"),

        FAIL_VALIDATION(4005, "数据校验不通过"),

        FAIL_OPERATION(4006, "操作执行失败"),

        FAIL_REQUEST_TIMEOUT(4008, "请求连接超时"),

        FAIL_TENANT_NOT_EXIST(4041, "租户不存在"),

        FAIL_USER_NOT_EXIST(4042, "用户不存在"),

        FAIL_EXCEPTION(5000, "系统异常"),

        FAIL_PERSISTENT_EXCEPTION(5001, "持久化失败"),

        FAIL_QUERY_EXCEPTION(5002, "查询失败"),

        FAIL_SERVICE_UNAVAILABLE(5003, "服务不可用"),

        FAIL_IO_EXCEPTION(5004, "系统IO异常"),

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
        public Status withMsg(String msg, Object[] args) {
            return Status.fail(this.iCode, msg, args);
        }
    }

    int getCode();

    String getMsg();

    Status withMsg(String msg);

    Status withMsg(String msg, Object[] args);

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
    public Status withMsg(String msg, Object[] args) {
        return Status.fail(this.code, msg, args);
    }

}
