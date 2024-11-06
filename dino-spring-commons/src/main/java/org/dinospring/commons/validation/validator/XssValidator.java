// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.validation.validator;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.validation.constraints.Xss;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 检测XSS注入
 * @author Cody Lu
 * @date 2022-04-06 19:38:07
 */

public class XssValidator implements ConstraintValidator<Xss, String> {

  @Override
  public void initialize(Xss constraintAnnotation) {
    //do nothing
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    return !value.contains("<") && !value.contains(">");
  }
}
