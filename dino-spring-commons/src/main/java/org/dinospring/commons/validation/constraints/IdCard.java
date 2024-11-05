// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.commons.validation.validator.ChinaIdCardValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 *
 * @author Cody LU
 * @date 2022-04-01 00:50:23
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { ChinaIdCardValidator.class })
public @interface IdCard {
  String message() default "{org.dinospring.validation.IdCard.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  // 国家，将根据该国家身份证信息进行验证，默认为中国
  Country country() default Country.CHINA;
}
