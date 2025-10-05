// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.commons.validation.validator.ChinaMobileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 手机号格式检查，对字符串进行手机号格式验证
 * @author Cody Lu
 * @since 2022-04-01
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { ChinaMobileValidator.class })
public @interface Mobile {

  /**
   * 验证失败时的错误信息
   * @return 错误信息模板
   */
  String message() default "{cn.dinodev.spring.validation.Mobile.message}";

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
   * 允许的国家，将对国家手机码进行验证
   * @return 允许的国家数组
   */
  Country[] allowedCountries() default { Country.CHINA };

  /**
   * 默认国家，如果没有指定国家，则使用默认国家
   * @return 默认国家枚举值
   */
  Country defaultCountry() default Country.CHINA;
}
