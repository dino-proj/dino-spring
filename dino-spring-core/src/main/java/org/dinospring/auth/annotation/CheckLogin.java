// Copyright 2022 dinospring.cn
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

/**
 * 用户登录校验，必须以指定的用户类型登录才能访问
 * <p> 标注在方法上，则此方法需通过验证才能访问</p>
 * <p> 标注在类上，则此类的所有方法都需通过验证才能访问</p>
 * <p> 标注在注解上，则该注解所标注的方法或类的所有方法，需要通过验证才能访问</p>
 * @author tuuboo
 * @date 2022-04-07 00:26:39
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
@Repeatable(CheckLogin.List.class)
public @interface CheckLogin {

  /**
   * 用户类型，默认为任意用户，多个用户类型之间为或关系
   * @return
   */
  String[] value() default {};

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Documented
  public @interface List {
    CheckLogin[] value();
  }
}
