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

package org.dinospring.commons.validation.validator;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.validation.constraints.Xss;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 检测XSS注入
 * @author Cody LU
 * @date 2022-04-06 19:38:07
 */

public class XssValidator implements ConstraintValidator<Xss, String> {

  @Override
  public void initialize(Xss constraintAnnotation) {
    //do nothing
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    return !value.contains("<") && !value.contains(">");
  }
}
