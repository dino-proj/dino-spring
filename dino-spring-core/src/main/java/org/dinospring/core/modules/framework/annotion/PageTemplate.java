// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.core.modules.framework.PageType;

/**
 *
 * @author Cody Lu
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageTemplate {

  /**
   * 页面模板名字
   * @return
   */
  String name();

  /**
   * 模板标题
   * @return
   */
  String title();

  /**
   * 页面类型
   * @return
   */
  PageType type();

  /**
   * 页面Icon图标地址
   * @return
   */
  String icon() default "";

  /**
   * app中页面路径
   * @return
   */
  String appPath() default "";

  /**
   * pc中页面路径，不填写，则同 {@link #appPath()}
   * @return
   */
  String pcPath() default "";

  /**
   * 对页面模板的描述
   * @return
   */
  String description() default "";
}
