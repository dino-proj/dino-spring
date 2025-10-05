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
 * IP地址验证注解
 * <p>用于验证IP地址的格式和有效性，支持IPv4、IPv6或任意版本
 *
 * @author Cody Lu
 * @since 2022-04-01
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { IpValidator.class })
public @interface Ip {

  /**
   * 验证失败时的错误信息
   * @return 错误信息模板
   */
  String message() default "{cn.dinodev.spring.validation.Ip.message}";

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

  /**
   * IP版本类型，默认为任意版本
   * @return IP版本枚举值
   */
  IpVersion version() default IpVersion.ANY;

  /**
   * IP版本枚举
   */
  enum IpVersion {
    /**
     * 任意版本
     */
    ANY,
    /**
     * IPv4
     */
    IPV4,
    /**
     * IPv6
     */
    IPV6
  }
}
