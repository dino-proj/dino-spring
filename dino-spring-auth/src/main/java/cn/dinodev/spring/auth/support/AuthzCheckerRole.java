// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.support;

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
import cn.dinodev.spring.auth.annotation.CheckIgnore;
import cn.dinodev.spring.auth.annotation.CheckRole;
import cn.dinodev.spring.auth.annotation.Logic;
import cn.dinodev.spring.auth.session.AuthSession;
import cn.dinodev.spring.commons.function.Predicates;

/**
 * 角色权限校验器
 * @author Cody Lu
 * @date 2022-04-09 17:48:24
 */

public class AuthzCheckerRole extends AbstractAuthzChecker<CheckRole, List<Predicate<AuthSession>>> {

  public AuthzCheckerRole() {
    super(CheckRole.class, CheckIgnore.Type.ROLE);
  }

  @Override
  protected List<Predicate<AuthSession>> getMethodInvocationMeta(MethodInvocation mi,
      Collection<CheckRole> annosInClass,
      Collection<CheckRole> annosInMethod) {

    return Stream.concat(annosInClass.stream(), annosInMethod.stream())
        .map(this::makeAnnoPredicate).collect(
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
    if (Logic.ALL.equals(roleAnno.logic())) {
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
      if (Objects.isNull(userType) && !this.userTypes.isEmpty()) {
        return false;
      }
      if (Objects.nonNull(userType) && !this.userTypes.contains(userType)) {
        return false;
      }
      return this.rolePredicate.test(session.getSubjectRoles());
    }
  }
}
