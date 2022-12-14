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

package org.dinospring.core.autoconfig;

import java.util.Optional;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.sys.Tenant;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *
 * @author tuuboo
 * @date 2022-06-29 20:10:44
 */

public class TenantArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    var param = parameter.nestedIfOptional();
    return Tenant.class.isAssignableFrom(param.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    var tenant = ContextHelper.currentUser();
    if (tenant != null && !parameter.getParameterType().isInstance(tenant)) {
      throw new IllegalStateException(
          "Current tenant is not of type [" + parameter.getParameterType().getName() + "]: " + tenant);
    }

    if (parameter.isOptional()) {
      return Optional.ofNullable(tenant);
    } else {
      return tenant;
    }
  }

}
