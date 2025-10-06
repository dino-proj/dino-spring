// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 允许访问注解，用于控制方法或类的访问权限
 *
 * @author Cody Lu
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface AllowAccess {

  /**
   * 用户类型
   * @return 用户类型字符串
   */
  String userType();

  /**
   * 任意权限数组，用户拥有其中任意一个权限即可访问
   * @return 权限字符串数组
   */
  String[] anyPermissions();

  /**
   * 全部权限数组，用户必须拥有所有权限才能访问
   * @return 权限字符串数组
   */
  String[] allPermissions();

  /**
   * 是否拒绝其他用户访问
   * @return true表示拒绝其他用户，false表示允许
   */
  boolean denyOthers() default false;
}
