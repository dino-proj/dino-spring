// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.commons.validation.validator.IpValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 *
 * @author Cody Lu
 * @date 2022-04-01 00:54:36
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { IpValidator.class })
public @interface Ip {

  String message() default "{cn.dinodev.spring.validation.Ip.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  // 默认为任意版本
  IpVersion version() default IpVersion.ANY;

  enum IpVersion {
    // 任意版本
    ANY,
    //IPv4
    IPV4,
    //IPv6
    IPV6
  }
}
