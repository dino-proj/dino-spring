// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.validator;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import cn.dinodev.spring.commons.validation.constraints.PasswordStrength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 密码强度校验器
 * <p>
 * 该校验器用于验证密码是否符合指定的强度要求，支持以下几种验证模式：
 * <ul>
 * <li>NUMERIC - 仅包含数字</li>
 * <li>NUMERIC_SPECIAL_CHARACTER - 包含数字和特殊字符</li>
 * <li>LETTER - 包含字母（可指定大小写要求）</li>
 * <li>NUMERIC_LETTER - 包含数字和字母</li>
 * <li>LETTER_SPECIAL_CHARACTER - 包含字母和特殊字符</li>
 * <li>NUMERIC_LETTER_SPECIAL_CHARACTER - 包含数字、字母和特殊字符</li>
 * </ul>
 * <p>
 * 对于字母类型，可以进一步指定：
 * <ul>
 * <li>ANY - 任意大小写字母</li>
 * <li>LOWER - 仅小写字母</li>
 * <li>UPPER - 仅大写字母</li>
 * <li>BOTH - 同时包含大小写字母</li>
 * </ul>
 * <p>
 * 特殊字符包括：)!@#$%^&*()
 *
 * @author Cody Lu
 * @since 2022-04-01
 */

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

  /**
   * 密码检查器数组，存储根据配置确定的检查规则
   */
  private PasswordChecker[] checkers;

  /**
   * 初始化校验器，根据注解配置确定密码强度检查规则
   *
   * @param constraintAnnotation 密码强度约束注解
   */
  @Override
  public void initialize(PasswordStrength constraintAnnotation) {
    checkers = switch (constraintAnnotation.format()) {
      case NUMERIC -> new PasswordChecker[] { PasswordChecker.NUMERIC };
      case NUMERIC_SPECIAL_CHARACTER -> new PasswordChecker[] {
          PasswordChecker.NUMERIC, PasswordChecker.SPECIAL_CHARACTER
      };
      case LETTER -> letterChecker(constraintAnnotation.letterType());
      case NUMERIC_LETTER -> ArrayUtils.addAll(
          letterChecker(constraintAnnotation.letterType()),
          PasswordChecker.NUMERIC);
      case LETTER_SPECIAL_CHARACTER -> ArrayUtils.addAll(
          letterChecker(constraintAnnotation.letterType()),
          PasswordChecker.SPECIAL_CHARACTER);
      case NUMERIC_LETTER_SPECIAL_CHARACTER -> ArrayUtils.addAll(
          letterChecker(constraintAnnotation.letterType()),
          PasswordChecker.NUMERIC,
          PasswordChecker.SPECIAL_CHARACTER);
    };
  }

  /**
   * 验证密码是否符合强度要求
   * <p>
   * 验证逻辑：
   * <ol>
   * <li>如果密码为空或空白，则视为有效（可由其他注解如@NotBlank处理非空验证）</li>
   * <li>遍历密码中的每个字符，检查是否满足所有配置的检查规则</li>
   * <li>只有当密码包含所有要求的字符类型时，才视为有效</li>
   * </ol>
   * </p>
   *
   * @param value 待验证的密码字符串
   * @param context 约束验证上下文
   * @return true 如果密码符合强度要求，false 否则
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true; // 空值交由其他验证器处理，如@NotBlank
    }
    // 为每种检查器创建命中标记数组
    boolean[] checkHits = new boolean[checkers.length];
    Arrays.fill(checkHits, false);

    // 遍历密码中的每个字符
    for (int i = 0; i < value.length(); i++) {
      char currentChar = value.charAt(i);
      // 对每个字符运行所有检查器
      for (int j = 0; j < checkers.length; j++) {
        if (checkers[j].check(currentChar)) {
          checkHits[j] = true; // 标记该类型字符已找到
        }
      }
    }
    // 只有所有检查器都命中时，密码才有效
    return !ArrayUtils.contains(checkHits, false);
  }

  /**
   * 根据字母类型要求创建相应的字母检查器
   *
   * @param type 字母类型要求
   * @return 对应的密码检查器数组
   */
  private static PasswordChecker[] letterChecker(PasswordStrength.LetterType type) {
    return switch (type) {
      case ANY -> new PasswordChecker[] { PasswordChecker.LETTER };
      case LOWER -> new PasswordChecker[] { PasswordChecker.LETTER_LOWER };
      case UPPER -> new PasswordChecker[] { PasswordChecker.LETTER_UPPER };
      case BOTH -> new PasswordChecker[] { PasswordChecker.LETTER_LOWER, PasswordChecker.LETTER_UPPER };
    };
  }

  /**
   * package-private
   * 密码字符类型检查器枚举
   * <p>
   * 定义了各种字符类型的检查逻辑，包括：
   * <ul>
   * <li>数字检查</li>
   * <li>字母检查（大小写）</li>
   * <li>特殊字符检查</li>
   * </ul>
   */
  private enum PasswordChecker {
    /**
     * 数字检查器
     * 检查字符是否为数字（0-9）
     */
    NUMERIC {
      @Override
      public boolean check(char ch) {
        return Character.isDigit(ch);
      }
    },
    /**
     * 字母检查器
     * 检查字符是否为字母（大写或小写）
     */
    LETTER {
      @Override
      public boolean check(char ch) {
        return Character.isLowerCase(ch) || Character.isUpperCase(ch);
      }
    },
    /**
     * 小写字母检查器
     * 检查字符是否为小写字母（a-z）
     */
    LETTER_LOWER {
      @Override
      public boolean check(char ch) {
        return Character.isLowerCase(ch);
      }
    },
    /**
     * 大写字母检查器
     * 检查字符是否为大写字母（A-Z）
     */
    LETTER_UPPER {
      @Override
      public boolean check(char ch) {
        return Character.isUpperCase(ch);
      }
    },
    /**
     * 特殊字符检查器
     * 检查字符是否为预定义的特殊字符
     */
    SPECIAL_CHARACTER {
      @Override
      public boolean check(char ch) {
        return Arrays.binarySearch(SYMBOLS, ch) >= 0;
      }
    };

    /**
     * 支持的特殊字符集合
     * 包含常用的密码特殊字符：)!@#$%^&*()
     */
    private static final char[] SYMBOLS = ")!@#$%^&*()".toCharArray();

    // 静态初始化块，对特殊字符数组进行排序以支持二分查找
    static {
      Arrays.sort(SYMBOLS);
    }

    /**
     * 检查指定字符是否符合当前检查器的要求
     *
     * @param ch 待检查的字符
     * @return true 如果字符符合要求，false 否则
     */
    public abstract boolean check(char ch);
  }
}
