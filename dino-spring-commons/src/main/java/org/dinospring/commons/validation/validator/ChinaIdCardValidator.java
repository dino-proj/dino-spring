// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.validation.validator;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.validation.constraints.IdCard;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 检查身份证号格式
 * @author Cody Lu
 * @date 2022-04-06 19:32:13
 */

public class ChinaIdCardValidator implements ConstraintValidator<IdCard, String> {

  @Override
  public void initialize(IdCard constraintAnnotation) {
    //do nothing
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    return value.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0-2]\\d)|3[0-1])\\d{3}([0-9X])$");
  }

}
