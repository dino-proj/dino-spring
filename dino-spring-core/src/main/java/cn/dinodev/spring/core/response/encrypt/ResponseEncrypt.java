// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.response.encrypt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 响应数据加密注解, 用于标记需要加密响应数据的方法
 * @author Cody Lu
 * @date 2025-09-17 18:48:09
 *
 * Usage:
 * @ResponseEncrypt
 * public Response<YourDataType> yourMethod() {
 *    // Your method implementation
 * }
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseEncrypt {

}
