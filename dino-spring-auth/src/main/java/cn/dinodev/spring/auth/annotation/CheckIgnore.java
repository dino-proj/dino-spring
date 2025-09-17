// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略权限检查，对于标注了此注解的类或接口，不会进行权限检查，忽略{@link CheckPermission}、{@link CheckLoginAs}、{@link CheckRole}注解
 * @author Cody Lu
 * @date 2022-04-11 21:51:33
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
public @interface CheckIgnore {

  /**
   * 忽略的类型，默认忽略所有类型，即忽略忽略{@link CheckPermission}、{@link CheckLoginAs}、{@link CheckRole}注解
   * @return
   */
  Type[] value() default {};

  public static enum Type {
    /**
     * 忽略权限检查
     */
    PERMISSION,
    /**
     * 忽略Subject Type检查
     */
    LOGIN_AS,
    /**
     * 忽略角色检查
     */
    ROLE
  }
}
