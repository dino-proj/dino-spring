// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.validator;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.commons.validation.constraints.Ip;

/**
 * 检查IP格式
 * @author Cody Lu
 * @date 2022-04-06 19:36:05
 */

public class IpValidator implements ConstraintValidator<Ip, String> {
  private static final Pattern PATTERN_V4 = Pattern
      .compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

  private static final Pattern PATTERN_V6 = Pattern
      .compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

  private Ip.IpVersion ver;

  @Override
  public void initialize(Ip constraintAnnotation) {
    ver = constraintAnnotation.version();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    return switch (ver) {
      case IPV4 -> PATTERN_V4.matcher(value).matches();
      case IPV6 -> PATTERN_V6.matcher(value).matches();
      case ANY -> PATTERN_V4.matcher(value).matches() ||
          PATTERN_V6.matcher(value).matches();
    };
  }
}
