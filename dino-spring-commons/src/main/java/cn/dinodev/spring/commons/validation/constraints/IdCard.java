// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.commons.validation.validator.ChinaIdCardValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 身份证号码验证注解
 * <p>用于验证身份证号码的格式和有效性
 *
 * @author Cody Lu
 * @since 2022-04-01
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { ChinaIdCardValidator.class })
public @interface IdCard {

  /**
   * 验证失败时的错误信息
   * @return 错误信息模板
   */
  String message() default "{cn.dinodev.spring.validation.IdCard.message}";

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
   * 国家，将根据该国家身份证信息进行验证，默认为中国
   * @return 国家枚举值
   */
  Country country() default Country.CHINA;
}
