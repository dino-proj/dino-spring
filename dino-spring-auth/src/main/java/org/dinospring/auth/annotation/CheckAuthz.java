// Copyright 2022 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.auth.AuthzChecker;

/**
 * 自定义权限认证，通过认证才能访问
 * <p> 标注在方法上，则此方法需通过验证才能访问</p>
 * <p> 标注在类上，则此类的所有方法都需通过验证才能访问</p>
 * <p> 标注在注解上，则该注解所有方法或类的所有方法，需要通过验证才能访问</p>
 *
 * @author Cody LU
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
   * <p>Bean必须实现{@link org.dinospring.auth.AuthzChecker}接口</p>
   *
   * @see org.dinospring.auth.AuthzChecker
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
