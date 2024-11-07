// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户登录校验，必须以指定的用户类型登录才能访问
 * <p> 标注在方法上，则此方法需通过验证才能访问</p>
 * <p> 标注在类上，则此类的所有方法都需通过验证才能访问</p>
 * <p> 标注在注解上，则该注解所标注的方法或类的所有方法，需要通过验证才能访问</p>
 * @author Cody Lu
 * @date 2022-04-07 00:26:39
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
@Repeatable(CheckLoginAs.List.class)
public @interface CheckLoginAs {

  /**
   * 用户类型，默认为任意用户，多个用户类型之间为或关系
   * @return
   */
  String[] value() default {};

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Documented
  public @interface List {
    CheckLoginAs[] value();
  }
}
