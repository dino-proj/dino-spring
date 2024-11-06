// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.json;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody Lu
 * @date 2022-06-09 02:49:56
 */

@UtilityClass
public class JsonViewUtils {

  /**
   * 判断是否在指定的视图中
   * @param activeView 视图
   * @param views 视图集合
   * @return true：在视图集合中，或者activeView==null， 或者views为空；false：不在视图集合中
   */
  public static boolean isInView(Class<?> activeView, Class<?>[] views) {
    return isInView(activeView, views, true);
  }

  /**
   * 判断是否在指定的视图中
   * @param activeView 视图
   * @param views 视图集合
   * @param defaultViewInclusion 是否默认包含默认视图
   * @return true：在视图集合中，或者activeView==null， 或者(views为空&&defaultViewInclusion==true)；false：不在视图集合中
   */

  public static boolean isInView(Class<?> activeView, Class<?>[] views, boolean defaultViewInclusion) {
    if (activeView == null) {
      return true;
    }
    if (views == null || views.length == 0) {
      return defaultViewInclusion;
    }

    final int len = views.length;
    for (int i = 0; i < len; ++i) {
      if (views[i].isAssignableFrom(activeView)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取方法的视图
   * @param element Method, Field, Constructor, Parameter, Class等
   * @return 视图，如果没有视图或者方法为null，则返回null
   */
  public static Class<?>[] findViews(AnnotatedElement element) {
    if (element == null) {
      return null;
    }
    var viewAnno = element.getDeclaredAnnotation(JsonView.class);
    if (Objects.nonNull(viewAnno)) {
      return viewAnno.value();
    }
    return null;
  }

  /**
   * 判断Method, Field, Constructor, Parameter, Class等是否在指定的视图中
   * @param element Method, Field, Constructor, Parameter, Class等
   * @param activeView 视图
   */
  public static boolean isInView(AnnotatedElement element, Class<?> activeView) {
    return isInView(activeView, findViews(element), true);
  }

  /**
   * 判断Method, Field, Constructor, Parameter, Class等是否在指定的视图中
   * @param element Method, Field, Constructor, Parameter, Class等
   * @param activeView 视图
   * @param defaultViewInclusion 是否默认包含默认视图
   * @return
   */
  public static boolean isInView(AnnotatedElement element, Class<?> activeView, boolean defaultViewInclusion) {
    return isInView(activeView, findViews(element), defaultViewInclusion);
  }
}
