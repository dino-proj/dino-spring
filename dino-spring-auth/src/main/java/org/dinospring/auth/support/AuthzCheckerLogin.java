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

package org.dinospring.auth.support;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.annotation.CheckLoginAs;
import org.dinospring.auth.session.AuthSession;

/**
 * 用户登录身份验证器
 * @author Cody LU
 * @date 2022-04-09 15:26:48
 */

public class AuthzCheckerLogin extends AbstractAuthzChecker<CheckLoginAs, List<String[]>> {

  public AuthzCheckerLogin() {
    super(CheckLoginAs.class, true);
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
