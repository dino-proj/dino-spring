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

package org.dinospring.commons.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.dinospring.commons.validation.validator.ChinaMobileValidator;

/**
 * 手机号格式检查，对字符串进行手机号格式验证
 * @author tuuboo
 * @date 2022-04-01 00:14:44
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { ChinaMobileValidator.class })
public @interface Mobile {
  String message() default "{org.dinospring.validation.Mobile.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  // 允许的国家，将对国家手机码进行验证
  Country[] allowedCountries() default { Country.CHINA };

  // 默认国家，如果没有指定国家，则使用默认国家
  Country defaultCountry() default Country.CHINA;
}
