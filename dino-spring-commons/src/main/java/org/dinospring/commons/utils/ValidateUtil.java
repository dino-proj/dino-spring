// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import lombok.experimental.UtilityClass;

/**
 *
 * @author tuuboo
 * @date 2022-03-07 21:54:54
 */

@UtilityClass
public class ValidateUtil {

  /**
   * 大陆号码或香港号码均可
   */
  public static boolean isMobile(String str) {
    return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
  }

  /**
   * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
   * 此方法中前三位格式有：
   * 13+任意数
   * 15+除4的任意数
   * 18+除1和4的任意数
   * 17+除9的任意数
   * 147
   */
  public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
    String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(19[0-9])|(18[0-9])|(17[0-8]))\\d{8}$";
    Pattern p = Pattern.compile(regExp);
    Matcher m = p.matcher(str);
    return m.matches();
  }

  /**
   * 香港手机号码8位数，5|6|8|9开头+7位任意数
   */
  private static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
    String regExp = "^([5-9])\\d{7}$";
    Pattern p = Pattern.compile(regExp);
    Matcher m = p.matcher(str);
    return m.matches();
  }

  public static boolean isRealName(String name) {
    String reg = "(([\\u4E00-\\u9FA5]{2,7})|([a-zA-Z]{3,10}))";
    Pattern p = Pattern.compile(reg);
    Matcher m = p.matcher(name);
    return m.matches();
  }

}
