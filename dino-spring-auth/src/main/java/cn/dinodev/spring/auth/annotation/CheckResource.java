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
 * 权限检查资源注解，用于标识Controller类对应的权限资源名称
 *
 * <p>该注解应用于Controller类上，用于定义该类下所有方法的权限资源前缀。
 * 结合方法级别的权限注解，可以构建完整的权限控制体系。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * &#64;RestController
 * &#64;CheckResource(name = "user", subjectType = {"admin", "user"})
 * public class UserController {
 *     &#64;CheckPermission("user:read")
 *     public User getUser() { ... }
 *
 *     &#64;CheckPermission("user:create")
 *     public User createUser() { ... }
 * }
 * </pre>
 *
 * <p>支持的配置选项：</p>
 * <ul>
 * <li>资源名称：定义权限资源的基础名称</li>
 * <li>用户类型限制：只对指定类型的用户生效</li>
 * <li>角色排除：具有特定角色的用户无需权限检查</li>
 * <li>用户类型排除：特定类型的用户无需权限检查</li>
 * </ul>
 *
 * @author Cody Lu
 * @date 2022-04-06 23:32:36
 * @see CheckPermission
 * @see CheckRole
 * @see CheckAuthz
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface CheckResource {

  /**
   * 资源的名称，与 {@link #name()} 互为别名
   *
   * <p>这是资源名称的简写形式，通常用于简单的配置场景。</p>
   *
   * @see #name()
   * @return 权限资源的名称，默认为空字符串
   */
  @AliasFor("name")
  String value() default "";

  /**
   * 资源的名称，与 {@link #value()} 互为别名
   *
   * <p>定义权限检查时使用的资源名称，这个名称会作为权限标识符的前缀。
   * 例如，如果资源名称为"user"，那么相关的权限可能包括"user:read"、"user:create"等。</p>
   *
   * @return 权限资源的名称，默认为空字符串
   */
  String name() default "";

  /**
   * 指定适用的用户类型，多个类型之间为OR关系
   *
   * <p>只有指定类型的用户才会进行权限检查，其他类型的用户将被忽略。
   * 如果不指定，则对所有用户类型都生效。</p>
   *
   * <p>示例：</p>
   * <pre>
   * subjectType = {"admin", "manager"} // 只对admin和manager用户进行权限检查
   * </pre>
   *
   * @return 适用的用户类型数组，默认为空（对所有用户类型生效）
   */
  String[] subjectType() default {};

  /**
   * 要排除的角色，具有这些角色的用户无需权限检查即可访问
   *
   * <p>当用户具备指定的任一角色时，将跳过权限检查直接允许访问。
   * 这通常用于给予某些高权限角色（如超级管理员）绕过特定权限检查的能力。</p>
   *
   * <p>示例：</p>
   * <pre>
   * exclueRoles = {"SUPER_ADMIN", "SYSTEM_ADMIN"} // 超级管理员和系统管理员可以绕过权限检查
   * </pre>
   *
   * @return 要排除的角色名称数组，默认为空
   */
  String[] exclueRoles() default {};

  /**
   * 要排除的用户类型，具有这些类型的用户无需权限检查即可访问
   *
   * <p>当用户的类型匹配指定的任一类型时，将跳过权限检查直接允许访问。
   * 这通常用于给予某些特殊用户类型（如系统用户、内部服务）绕过权限检查的能力。</p>
   *
   * <p>注意：这与 {@link #subjectType()} 的作用相反：</p>
   * <ul>
   * <li>{@code subjectType}：只对指定类型的用户进行权限检查</li>
   * <li>{@code exclueSubjectTypes}：对指定类型的用户跳过权限检查</li>
   * </ul>
   *
   * <p>示例：</p>
   * <pre>
   * exclueSubjectTypes = {"SYSTEM", "SERVICE"} // 系统用户和服务用户可以绕过权限检查
   * </pre>
   *
   * @return 要排除的用户类型名称数组，默认为空
   */
  String[] exclueSubjectTypes() default {};
}
