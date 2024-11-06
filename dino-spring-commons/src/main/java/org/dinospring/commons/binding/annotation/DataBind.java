// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package org.dinospring.commons.binding.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动绑定注解
 * @author Cody Lu
 * @date 2022-04-13 04:12:27
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface DataBind {

  /**
   * 处理此绑定的bean name，此属性和{@link #beanClass()}必须设置一个，如果都设置，则同时使用Name和Class.
   * <p>** 支持spring {@linkplain https://docs.spring.io/spring-framework/docs/5.3.20/reference/html/core.html#expressions SpEL表达式}  **</p>
   * @return
   */
  String beanName() default "";

  /**
   * 处理此绑定的bean class，此属性和{@link #beanName()}必须设置一个，如果都设置，则同时使用Name和Class
   * @return
   */
  Class<?> beanClass() default Object.class;

  /**
   * 获取绑定的ID属性的路径，此路径将通过
   * @return
   */
  String[] idPath() default {};
}
