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
 * 角色检查，用户必须拥有指定的角色才能访问
 * <p> 标注在方法上，则此方法需通过验证才能访问</p>
 * <p> 标注在类上，则此类的所有方法都需通过验证才能访问</p>
 * <p> 标注在注解上，则该注解所有方法或类的所有方法，需要通过验证才能访问</p>
 *
 * @author Cody Lu
 * @date 2022-04-07 00:06:17
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
@Repeatable(CheckRole.List.class)
public @interface CheckRole {

  /**
   * <p>角色表达式，多个表达式之间的关系由{@link #logic()}指定</p>
   *
   * <p>单个权限表达式可以用‘|’(或)或者‘&’(与)连接，如：</p>
   * <p>- {@code "userman | tenantman"}, 表示需具备”用户管理“或者”租户管理“角色</p>
   * <p>- {@code "userman & tenantman"}, 表示需同时具备”用户管理“和”租户管理“角色</p>
   * <p>*** 注意：‘|’和‘&’不能在同一个表达式混用，否则会抛出异常 ***</p>
   *
   * <p>角色表达式还可以和{@link #logic()}指定的逻辑关系组合使用，如：</p>
   * <pre>
   * &#64;CheckRole({"userman", "tenantman"}, Logic.ALL) // 表示"userman" && "tenantman"
   * &#64;CheckRole({"userman", "tenantman"}) // 表示"userman" || "tenantman"
   * &#64;CheckRole({"userman | tenantman", "orgman"}) // 表示("userman" || "tenantman") && "orgman"
   * </pre>
   *
   * @return
   */
  String[] value();

  /**
   * 用户类型，默认为任意用户，多个用户类型之间为或关系
   * @return
   */
  String[] subjectType() default {};

  /**
   * 多个权限表达式的逻辑关系
   */
  Logic logic() default Logic.ANY;

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Documented
  public @interface List {
    CheckRole[] value();
  }
}
