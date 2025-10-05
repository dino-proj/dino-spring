// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * 数据验证工具类，提供各种数据验证的便捷方法
 *
 * @author Cody Lu
 * @since 2022-03-07
 */

@UtilityClass
public class ValidateUtil {

  /**
   * 验证是否为合法手机号码（支持大陆号码或香港号码）
   * @param str 待验证的手机号码字符串
   * @return 是否为合法手机号码
   */
  public static boolean isMobile(String str) {
    return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
  }

  /**
   * 验证大陆手机号码是否合法
   * <p>大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
   * <p>此方法中前三位格式有：
   * <ul>
   * <li>13+任意数</li>
   * <li>15+除4的任意数</li>
   * <li>18+除1和4的任意数</li>
   * <li>17+除9的任意数</li>
   * <li>147</li>
   * </ul>
   * @param str 待验证的手机号码字符串
   * @return 是否为合法的大陆手机号码
   */
  public static boolean isChinaPhoneLegal(String str) {
    String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(19[0-9])|(18[0-9])|(17[0-8]))\\d{8}$";
    Pattern pattern = Pattern.compile(regExp);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

  /**
   * 验证香港手机号码是否合法
   * <p>香港手机号码8位数，5|6|8|9开头+7位任意数
   * @param str 待验证的手机号码字符串
   * @return 是否为合法的香港手机号码
   */
  private static boolean isHKPhoneLegal(String str) {
    String regExp = "^([5-9])\\d{7}$";
    Pattern pattern = Pattern.compile(regExp);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

  /**
   * 验证是否为真实姓名
   * @param name 姓名字符串
   * @return 是否为真实姓名格式
   */
  public static boolean isRealName(String name) {
    String reg = "(([\\u4E00-\\u9FA5]{2,7})|([a-zA-Z]{3,10}))";
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(name);
    return matcher.matches();
  }

}
