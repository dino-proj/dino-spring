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

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.validation.constraints.Ip;

/**
 * 检查IP格式
 * @author Cody LU
 * @date 2022-04-06 19:36:05
 */

public class IpValidator implements ConstraintValidator<Ip, String> {
  private static final Pattern PATTERN_V4 = Pattern
      .compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

  private static final Pattern PATTERN_V6 = Pattern
      .compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

  private Ip.IpVersion ver;

  @Override
  public void initialize(Ip constraintAnnotation) {
    ver = constraintAnnotation.version();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return true;
    }
    if (ver == Ip.IpVersion.IPV4) {
      return PATTERN_V4.matcher(value).matches();
    } else if (ver == Ip.IpVersion.IPV6) {
      return PATTERN_V6.matcher(value).matches();
    } else {
      return PATTERN_V4.matcher(value).matches() || PATTERN_V6.matcher(value).matches();
    }
  }
}
