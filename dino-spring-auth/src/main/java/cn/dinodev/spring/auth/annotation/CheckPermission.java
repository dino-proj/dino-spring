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
 * 权限检查，必须具备权限才能访问
 * <p> 标注在方法上，则此方法需通过验证才能访问</p>
 * <p> 标注在类上，则此类的所有方法都需通过验证才能访问</p>
 * <p> 标注在注解上，则该注解所有方法或类的所有方法，需要通过验证才能访问</p>
 * @author Cody Lu
 * @date 2022-04-06 22:32:20
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
@Repeatable(CheckPermission.List.class)
public @interface CheckPermission {
  /**
   * <p>权限表达式，多个表达式之间的关系由{@link #logic()}指定</p>
   * <p>权限命名空间由':'分隔，用*表示任意匹配符，如：</p>
   * <p>- {@code "user:create"}，表示用户创建权限</p>
   * <p>- {@code "user:*"}，表示用户任意权限</p>
   *
   * <p>单个权限表达式可以用‘|’(或)或者‘&’(与)连接，如：</p>
   * <p>- {@code "user:create | user:update"}, 表示需具备用户“创建”或者“更新”权限</p>
   * <p>- {@code "user:create & user:update"}, 表示需同时具备用户“创建”和“更新“权限</p>
   * <p>*** 注意：‘|’和‘&’不能在同一个表达式混用，否则会抛出异常 ***</p>
   *
   * <p>权限可以和{@link #logic()}指定的逻辑关系组合使用，如：</p>
   * <pre>
   * &#64;CheckPermission({"user:create", "user:update"}, Logic:ALL) // 表示"user:create" && "user:update"
   * &#64;CheckPermission({"user:create", "user:update"}) // 表示"user:create" || "user:update"
   * &#64;CheckPermission({"user:create | user:update", "user:delete"}) // 表示("user:create" || "user:update") && "user:delete"
   * </pre>
   *
   * <p>权限表达式还可以和{@link #CheckResource#value()}指定的资源类型组合使用，如：</p>
   * <pre>
   *
   *  &#64;CheckResource("user")
   * Class A {
   *   &#64;CheckPermission(":create|:update")
   *   public void create() {
   *   }
   * }
   * 则表示"user:create|user:update"
   * </pre>
   *
   * @return
   */
  String[] value();

  /**
   * 用户类型，默认为任意用户，多个用户类型之间为或关系
   * 只对该类型的用户生效，其他类型的用户不适用
   * @return
   */
  String[] subjectType() default {};

  /**
   * 多个权限表达式的逻辑关系
   * @return
   */
  Logic logic() default Logic.ANY;

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

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Documented
  public @interface List {
    CheckPermission[] value();
  }
}
