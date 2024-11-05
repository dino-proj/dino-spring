// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.validation.validator;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.validation.constraints.Mobile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 检查手机号格式
 * @author Cody LU
 * @date 2022-04-01 00:09:34
 */

public class ChinaMobileValidator implements ConstraintValidator<Mobile, String> {

  @Override
  public void initialize(Mobile constraintAnnotation) {
    //do nothing
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    return value.matches("^1[3-9]\\d{9}$");
  }
}
