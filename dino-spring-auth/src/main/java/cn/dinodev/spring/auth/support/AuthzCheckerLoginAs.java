// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.support;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.auth.annotation.CheckIgnore;
import cn.dinodev.spring.auth.annotation.CheckLoginAs;
import cn.dinodev.spring.auth.session.AuthSession;

/**
 * 用户登录身份验证器
 * @author Cody Lu
 * @date 2022-04-09 15:26:48
 */

public class AuthzCheckerLoginAs extends AbstractAuthzChecker<CheckLoginAs, List<String[]>> {

  public AuthzCheckerLoginAs() {
    super(CheckLoginAs.class, CheckIgnore.Type.LOGIN_AS);
  }

  @Override
  protected List<String[]> getMethodInvocationMeta(MethodInvocation mi, Collection<CheckLoginAs> annosInClass,
      Collection<CheckLoginAs> annosInMethod) {
    var all = Stream.concat(annosInClass.stream(), annosInMethod.stream())
        .map(t -> dealUserTypes(t.value())).filter(Objects::nonNull)
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(all)) {
      return null;
    }
    return all;
  }

  public static String[] dealUserTypes(String[] userType) {
    userType = StringUtils.stripAll(userType);
    userType = ArrayUtils.removeAllOccurrences(userType, "");

    if (userType == null || userType.length == 0) {
      return null;
    }
    return userType;
  }

  @Override
  protected boolean isPermmited(AuthSession session, MethodInvocation mi, List<String[]> meta) {
    if (session == null || session.getSubjectType() == null) {
      return false;
    }
    for (var userType : meta) {
      if (ArrayUtils.contains(userType, session.getSubjectType())) {
        return true;
      }
    }
    return false;
  }

}
