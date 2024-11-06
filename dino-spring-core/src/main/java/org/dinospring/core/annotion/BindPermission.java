// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定IAM权限 - 添加在Controller上的注解，可以支持：
 * 1. 自动将此注解code值作为前缀添加至Mapping方法上的Shiro注解的value中
 * 2. 注解自动入库，自动更新
 *
 * @author Cody Lu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface BindPermission {
  /**
   * 名称
   * 设置当前权限名称
   * @return
   */
  String name();

  /**
   * 当前权限编码，默认为Entity类名
   * @return
   */
  String code() default "";
}
