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

package org.dinospring.auth.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.annotation.CheckRole;
import org.dinospring.auth.annotation.Logic;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.commons.function.Predicates;

/**
 * 角色权限校验器
 * @author tuuboo
 * @date 2022-04-09 17:48:24
 */

public class AuthzCheckerRole extends AbstractAuthzChecker<CheckRole, List<Predicate<AuthSession>>> {

  public AuthzCheckerRole() {
    super(CheckRole.class, true);
  }

  @Override
  protected List<Predicate<AuthSession>> getMethodInvocationMeta(MethodInvocation mi,
      Collection<CheckRole> annosInClass,
      Collection<CheckRole> annosInMethod) {

    return Stream.concat(annosInClass.stream(), annosInMethod.stream())
        .map(t -> makeAnnoPredicate(t)).collect(
            Collectors.toList());
  }

  @Override
  protected boolean isPermmited(AuthSession session, MethodInvocation mi, List<Predicate<AuthSession>> predicates) {
    for (var predicate : predicates) {
      if (!predicate.test(session)) {
        return false;
      }
    }
    return true;
  }

  private Predicate<AuthSession> makeAnnoPredicate(CheckRole roleAnno) {
    var rules = Arrays.asList(roleAnno.value()).stream().filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    if (rules.isEmpty()) {
      throw new IllegalArgumentException("@CheckRole.value must have at least one rule");
    }
    var predicates = rules.stream().map(AuthzCheckerRole::makeRolePredicate).collect(Collectors.toList());

    if (predicates.size() == 1) {
      return new RoleAnnoPredicate(predicates.get(0), roleAnno.subjectType());
    }
    if (roleAnno.logic().equals(Logic.ALL)) {
      return new RoleAnnoPredicate(Predicates.and(predicates), roleAnno.subjectType());
    } else {
      return new RoleAnnoPredicate(Predicates.or(predicates), roleAnno.subjectType());
    }
  }

  public static Predicate<Collection<String>> makeRolePredicate(String roleExpress) {
    var containsAny = StringUtils.contains(roleExpress, '|');
    var containsAll = StringUtils.contains(roleExpress, '&');
    if (containsAny && containsAll) {
      throw new IllegalArgumentException("@CheckRole.value can't contain both '|' and '&'");
    }
    if (containsAny) {
      var roles = StringUtils.stripAll(StringUtils.split(roleExpress, '|'));
      return Predicates.hasAny(roles);

    }
    if (containsAll) {
      var roles = StringUtils.stripAll(StringUtils.split(roleExpress, '&'));
      return Predicates.hasAll(roles);
    }

    return Predicates.hasAny(StringUtils.strip(roleExpress));

  }

  private static class RoleAnnoPredicate implements Predicate<AuthSession> {

    private final Predicate<Collection<String>> rolePredicate;
    private final Set<String> userTypes;

    public RoleAnnoPredicate(Predicate<Collection<String>> rolePredicate, String[] userTypes) {
      this.rolePredicate = rolePredicate;
      this.userTypes = new HashSet<>(Arrays.asList(userTypes));
    }

    @Override
    public boolean test(AuthSession session) {
      //check user type
      var userType = session.getSubjectType();
      if (Objects.isNull(userType) && !userTypes.isEmpty()) {
        return false;
      }
      if (Objects.nonNull(userType) && !userTypes.contains(userType)) {
        return false;
      }
      return rolePredicate.test(session.getSubjectRoles());
    }
  }
}
