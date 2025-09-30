// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.validator;

import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.commons.validation.constraints.Mobile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 检查手机号格式的验证器
 * @author Cody Lu
 * @since 2022-04-01
 */

public class ChinaMobileValidator implements ConstraintValidator<Mobile, String> {

  /**
   * 初始化验证器
   * @param constraintAnnotation 约束注解
   */
  @Override
  public void initialize(Mobile constraintAnnotation) {
    //do nothing
  }

  /**
   * 验证手机号格式
   * @param value 待验证的手机号
   * @param context 验证上下文
   * @return 验证结果，true表示格式正确，false表示格式错误
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    return value.matches("^1[3-9]\\d{9}$");
  }
}
