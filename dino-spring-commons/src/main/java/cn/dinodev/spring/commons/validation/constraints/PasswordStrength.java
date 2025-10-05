// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.commons.validation.validator.PasswordStrengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 密码强度验证注解
 * <p>用于验证密码的复杂性和强度，支持多种格式和字母类型组合
 *
 * @author Cody Lu
 * @since 2022-04-01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { PasswordStrengthValidator.class })
public @interface PasswordStrength {

  /**
   * 验证失败时的错误信息
   * @return 错误信息模板
   */
  String message() default "{cn.dinodev.spring.validation.PasswordStrength.message}";

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
   * 密码格式配置，默认为纯数字+字母+特殊字符
   * <p>支持的格式类型：
   * <ul>
   * <li>纯数字</li>
   * <li>纯字母</li>
   * <li>数字+字母</li>
   * <li>数字+特殊字符</li>
   * <li>字母+特殊字符</li>
   * <li>数字+字母+特殊字符</li>
   * </ul>
   * @return 密码格式枚举值
   */
  Format format() default Format.NUMERIC_LETTER_SPECIAL_CHARACTER;

  /**
   * 字母类型配置，默认为大小写字母都可以
   * <p>支持的字母类型：
   * <ul>
   * <li>仅大写字母</li>
   * <li>仅小写字母</li>
   * <li>大小写字母都可以</li>
   * <li>必须包含大小写字母</li>
   * </ul>
   * @return 字母类型枚举值
   */
  LetterType letterType() default LetterType.ANY;

  /**
   * 字母类型枚举
   * <p>定义密码中字母的大小写要求
   */
  enum LetterType {
    /**
     * 仅允许小写字母
     */
    LOWER,
    /**
     * 仅允许大写字母
     */
    UPPER,
    /**
     * 允许大小写字母（可以是大写或小写）
     */
    ANY,
    /**
     * 必须同时包含大写和小写字母
     */
    BOTH
  }

  /**
   * 密码格式枚举
   * <p>定义密码中允许的字符类型组合
   */
  enum Format {
    /**
     * 仅包含数字字符（0-9）
     */
    NUMERIC,
    /**
     * 仅包含字母字符（a-z, A-Z）
     */
    LETTER,
    /**
     * 包含数字和字母字符
     */
    NUMERIC_LETTER,
    /**
     * 包含数字和特殊字符
     */
    NUMERIC_SPECIAL_CHARACTER,
    /**
     * 包含字母和特殊字符
     */
    LETTER_SPECIAL_CHARACTER,
    /**
     * 包含数字、字母和特殊字符（最强密码格式）
     */
    NUMERIC_LETTER_SPECIAL_CHARACTER
  }
}