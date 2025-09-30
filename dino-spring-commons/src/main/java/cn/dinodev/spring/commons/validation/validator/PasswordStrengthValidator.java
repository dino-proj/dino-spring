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
 * @author Cody Lu
 * @date 2022-04-01 01:03:58
 */

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

  private PasswordChecker[] checkers;

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

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    boolean[] checkHits = new boolean[checkers.length];
    Arrays.fill(checkHits, false);
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      for (int j = 0; j < checkers.length; j++) {
        if (checkers[j].check(c)) {
          checkHits[j] = true;
        }
      }
    }
    return !ArrayUtils.contains(checkHits, false);
  }

  private static PasswordChecker[] letterChecker(PasswordStrength.LetterType type) {
    return switch (type) {
      case ANY -> new PasswordChecker[] { PasswordChecker.LETTER };
      case LOWER -> new PasswordChecker[] { PasswordChecker.LETTER_LOWER };
      case UPPER -> new PasswordChecker[] { PasswordChecker.LETTER_UPPER };
      case BOTH -> new PasswordChecker[] { PasswordChecker.LETTER_LOWER, PasswordChecker.LETTER_UPPER };
    };
  }

  /**
   * 密码校验器
   */
  enum PasswordChecker {
    // 数字
    NUMERIC {
      @Override
      boolean check(char ch) {
        return Character.isDigit(ch);
      }
    },
    // 大小写字母
    LETTER {
      @Override
      boolean check(char ch) {
        return Character.isLowerCase(ch) || Character.isUpperCase(ch);
      }
    },
    // 小写字母
    LETTER_LOWER {
      @Override
      boolean check(char ch) {
        return Character.isLowerCase(ch);
      }
    },
    // 大写字母
    LETTER_UPPER {
      @Override
      boolean check(char ch) {
        return Character.isUpperCase(ch);
      }
    },
    // 特殊字符
    SPECIAL_CHARACTER {
      @Override
      boolean check(char ch) {
        return Arrays.binarySearch(SYMBOLS, ch) >= 0;
      }
    };

    private static final char[] SYMBOLS = ")!@#$%^&*()".toCharArray();
    static {
      Arrays.sort(SYMBOLS);
    }

    abstract boolean check(char ch);
  }
}
