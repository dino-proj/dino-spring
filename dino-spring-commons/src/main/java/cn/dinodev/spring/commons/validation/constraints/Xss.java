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
 * Xss检查，对字符串进行Xss检查
 * @author Cody Lu
 * @date 2022-03-31 23:42:45
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { XssValidator.class })
public @interface Xss {
  String message() default "{cn.dinodev.spring.validation.Xss.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}