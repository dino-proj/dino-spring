// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.validation.validator;

import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.commons.validation.constraints.Xss;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * XSS（跨站脚本攻击）注入检测校验器
 * <p>
 * 该校验器用于检测输入字符串中是否包含可能导致XSS攻击的字符，
 * 主要检测HTML标签的开始符号'&lt;'和结束符号'&gt;'。
 * </p>
 * <p>
 * 检测规则：
 * <ul>
 * <li>如果字符串为空或空白，则视为有效</li>
 * <li>如果字符串包含'&lt;'或'&gt;'字符，则视为无效（可能存在XSS风险）</li>
 * <li>其他情况视为有效</li>
 * </ul>
 * </p>
 * <p>
 * 注意：这是一个基础的XSS检测实现，适用于简单的场景。
 * 对于更复杂的XSS防护需求，建议使用专业的HTML清理库。
 * </p>
 *
 * @author Cody Lu
 * @since 2022-04-06
 */

public class XssValidator implements ConstraintValidator<Xss, String> {

  /**
   * 初始化校验器
   * <p>
   * 该方法在校验器实例创建时被调用，用于进行必要的初始化操作。
   * 对于XSS校验器，无需特殊的初始化逻辑。
   * </p>
   *
   * @param constraintAnnotation XSS约束注解实例
   */
  @Override
  public void initialize(Xss constraintAnnotation) {
    //do nothing
  }

  /**
   * 执行XSS注入检测
   * <p>
   * 检测输入字符串是否包含可能导致XSS攻击的字符。
   * 当前实现主要检测HTML标签的开始和结束符号。
   * </p>
   *
   * @param value 待检测的字符串值
   * @param context 约束验证上下文
   * @return true 如果字符串不包含XSS风险字符，false 如果包含风险字符
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    // 检测是否包含HTML标签符号，这些可能被用于XSS攻击
    return !value.contains("<") && !value.contains(">");
  }
}
