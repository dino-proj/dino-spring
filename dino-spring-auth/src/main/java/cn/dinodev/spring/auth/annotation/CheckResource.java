// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 权限所对应的资源的名称
 * @author Cody Lu
 * @date 2022-04-06 23:32:36
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface CheckResource {

  /**
   * 资源的名称
   * @see #name()
   * @return
   */
  @AliasFor("name")
  String value() default "";

  /**
   * 资源的名称
   * @return
   */
  String name() default "";

  /**
  * 用户类型，默认为任意用户，多个用户类型之间为或关系
  * 只对该类型的用户生效，其他类型的用户不适用
  * @return
  */
  String[] subjectType() default {};

  /**
  * 要排除的角色，当用户具备此角色时，不需要权限即可访问
  * @return
  */
  String[] exclueRoles() default {};

  /**
   * 要排除的用户类型，当用户具备此用户类型时，不需要权限即可访问
   * @return
   */
  String[] exclueSubjectTypes() default {};
}
