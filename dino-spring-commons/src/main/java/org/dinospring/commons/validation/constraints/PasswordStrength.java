// Copyright 2022 dinodev.cn
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

package org.dinospring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.dinospring.commons.validation.validator.PasswordStrengthValidator;

/**
 * 密码强度
 * @author tuuboo
 * @date 2022-04-01 01:32:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { PasswordStrengthValidator.class })
public @interface PasswordStrength {

  String message() default "{org.dinospring.validation.PasswordStrength.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  // 密码格式，默认为纯数字，可选为纯字母，纯数字+字母，纯数字+特殊字符，纯字母+特殊字符，纯数字+字母+特殊字符
  Format format() default Format.NUMERIC_LETTER_SPECIAL_CHARACTER;

  // 字母类型，默认为大小写字母都可以，可选为大写字母，小写字母，大小写都可以，大小写都需要
  LetterType letterType() default LetterType.ANY;

  /**
   * 字母类型
   */
  enum LetterType {
    // 小写字母
    LOWER,
    // 大写字母
    UPPER,
    // 大小写都可以
    ANY,
    // 大小写都需要
    BOTH
  }

  /**
   * 密码格式
   */
  enum Format {
    // 纯数字
    NUMERIC,
    // 纯字母
    LETTER,
    // 纯数字+字母
    NUMERIC_LETTER,
    // 纯数字+特殊字符
    NUMERIC_SPECIAL_CHARACTER,
    // 纯字母+特殊字符
    LETTER_SPECIAL_CHARACTER,
    // 纯数字+字母+特殊字符
    NUMERIC_LETTER_SPECIAL_CHARACTER
  }
}