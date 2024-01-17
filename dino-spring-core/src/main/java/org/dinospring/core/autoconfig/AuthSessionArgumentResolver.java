// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.autoconfig;

import java.util.Optional;

import org.dinospring.auth.DinoAuth;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.sys.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *
 * @author Cody Lu
 * @date 2022-06-29 20:10:44
 */

public class AuthSessionArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    var param = parameter.nestedIfOptional();
    return AuthSession.class.isAssignableFrom(param.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    var authSession = DinoAuth.getAuthSession();
    if (authSession != null && !parameter.getParameterType().isInstance(authSession)) {
      throw new IllegalStateException(
          "authSession is not of type [" + parameter.getParameterType().getName() + "]: " + authSession);
    }

    if (parameter.isOptional()) {
      return Optional.ofNullable(authSession);
    } else {
      return authSession;
    }
  }

}
