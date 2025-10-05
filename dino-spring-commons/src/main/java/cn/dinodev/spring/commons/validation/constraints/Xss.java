// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.commons.validation.validator.XssValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * XSS攻击防护验证注解
 * <p>用于检测和防止跨站脚本攻击（XSS）的字符串内容验证
 *
 * @author Cody Lu
 * @since 2022-03-31
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { XssValidator.class })
public @interface Xss {

  /**
   * 验证失败时的错误信息
   * @return 错误信息模板
   */
  String message() default "{cn.dinodev.spring.validation.Xss.message}";

  /**
   * 验证组
   * @return 验证组数组
   */
  Class<?>[] groups() default {};

  /**
   * 负载信息
   * @return 负载信息数组
   */
  Class<? extends Payload>[] payload() default {};
}