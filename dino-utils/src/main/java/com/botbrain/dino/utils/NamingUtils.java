package com.botbrain.dino.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NamingUtils {

  /**
   * 下划线风格命名转换为驼峰
   * @param name 下划线风格
   * @return 驼峰
   */
  public static String toCamel(String name) {
    if (StringUtils.isBlank(name))
      return name; // garbage in, garbage out
    StringBuilder result = new StringBuilder();
    boolean nextIsUpper = false;
    if (name.length() > 1 && name.charAt(1) == '_') {
      result.append(Character.toUpperCase(name.charAt(0)));
    } else {
      result.append(Character.toLowerCase(name.charAt(0)));
    }
    for (int i = 1; i < name.length(); i++) {
      char c = name.charAt(i);
      if (c == '_') {
        nextIsUpper = true;
      } else {
        if (nextIsUpper) {
          result.append(Character.toUpperCase(c));
          nextIsUpper = false;
        } else {
          result.append(Character.toLowerCase(c));
        }
      }
    }
    return result.toString();
  }

  /**
   * 将名字转为下划线风格的命名
   * 
     * <ul><li>&quot;userName&quot; 转换为 &quot;user_name&quot;</li>
     * <li>&quot;UserName&quot; 转换为 &quot;user_name&quot;</li>
     * <li>&quot;USER_NAME&quot; 转换为 &quot;user_name&quot;</li>
     * <li>&quot;user_name&quot; 转换为 &quot;user_name&quot; (unchanged)</li>
     * <li>&quot;user&quot; 转换为 &quot;user&quot; (unchanged)</li>
     * <li>&quot;User&quot; 转换为 &quot;user&quot;</li>
     * <li>&quot;USER&quot; 转换为 &quot;user&quot;</li>
     * <li>&quot;_user&quot; 转换为 &quot;user&quot;</li>
     * <li>&quot;_User&quot; 转换为 &quot;user&quot;</li>
     * <li>&quot;__user&quot; 转换为 &quot;_user&quot; 
     * (两个下划线，只保留一个)</li>
     * <li>&quot;user__name&quot; 转换为 &quot;user__name&quot;
     * (保持不变, 两个下划线)</li></ul>
     *
   * @param name
   * @return
   */
  public static String toSnake(String name) {
    if (name == null)
      return name; // garbage in, garbage out
    int length = name.length();
    StringBuilder result = new StringBuilder(length * 2);
    int resultLength = 0;
    boolean wasPrevTranslated = false;
    for (int i = 0; i < length; i++) {
      char c = name.charAt(i);
      if (i > 0 || c != '_') // skip first starting underscore
      {
        if (Character.isUpperCase(c)) {
          if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
            result.append('_');
            resultLength++;
          }
          c = Character.toLowerCase(c);
          wasPrevTranslated = true;
        } else {
          wasPrevTranslated = false;
        }
        result.append(c);
        resultLength++;
      }
    }
    return resultLength > 0 ? result.toString() : name;
  }

  /**
   * 从
   */
  public static String methodToProperty(String name) {
    if (name.startsWith("is")) {
      name = name.substring(2);
    } else if (name.startsWith("get") || name.startsWith("set")) {
      name = name.substring(3);
    } else {
      throw new IllegalArgumentException(
          "Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
    }

    if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
      name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
    }
    return name;
  }
}
