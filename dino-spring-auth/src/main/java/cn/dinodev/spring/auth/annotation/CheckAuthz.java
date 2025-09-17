// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.auth.AuthzChecker;

/**
 * 自定义权限认证，通过认证才能访问
 * <p> 标注在方法上，则此方法需通过验证才能访问</p>
 * <p> 标注在类上，则此类的所有方法都需通过验证才能访问</p>
 * <p> 标注在注解上，则该注解所有方法或类的所有方法，需要通过验证才能访问</p>
 *
 * @author Cody Lu
 * @date 2022-04-07 00:42:04
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
@Repeatable(CheckAuthz.List.class)
public @interface CheckAuthz {

  /**
   * <p>权限验证的Bean名称，多个Bean之间关系由{@link #logic()}指定</p>
   * <p>Bean需要实现{@link AuthzChecker}接口</p>
   * <pre>
   * &#64;CheckAuthz({"mobileAuthz", "mfaAuthz"}, Logic.ALL) // 表示"mobileAuthz" && "mfaAuthz"
   * &#64;CheckAuthz({"mobileAuthz", "mfaAuthz"}) // 表示"mobileAuthz" || "mfaAuthz"
   * </pre>
   *
   * <p>Bean必须实现{@link cn.dinodev.spring.auth.AuthzChecker}接口</p>
   *
   * @see cn.dinodev.spring.auth.AuthzChecker
   * @return
   */
  String[] value();

  Class<? extends AuthzChecker>[] beanClass() default {};

  /**
   * 多个bean表达式的逻辑关系
   */
  Logic logic() default Logic.ANY;

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Documented
  public @interface List {
    CheckAuthz[] value();
  }
}
