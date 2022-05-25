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
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.Permission;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.auth.annotation.CheckResource;
import org.dinospring.auth.annotation.Logic;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.commons.function.Predicates;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * 用户权限校验器
 * @author tuuboo
 * @date 2022-04-09 15:27:04
 */

public class AuthzCheckerPermission extends AbstractAuthzChecker<CheckPermission, List<Predicate<AuthSession>>> {

  public AuthzCheckerPermission() {
    super(CheckPermission.class, true);
  }

  @Override
  protected List<Predicate<AuthSession>> getMethodInvocationMeta(MethodInvocation mi,
      Collection<CheckPermission> annosInClass, Collection<CheckPermission> annosInMethod) {
    var resource = AnnotatedElementUtils.getMergedAnnotation(mi.getThis().getClass(), CheckResource.class);

    var resourceName = Optional.ofNullable(resource).map(t -> t.value()).orElse(null);

    return Stream.concat(annosInClass.stream(), annosInMethod.stream())
        .map(t -> makeAnnoPredicate(t, resourceName)).collect(
            Collectors.toList());

  }

  @Override
  protected boolean isPermmited(AuthSession session, MethodInvocation mi,
      List<Predicate<AuthSession>> predicates) {
    for (var predicate : predicates) {
      if (!predicate.test(session)) {
        return false;
      }
    }
    return true;
  }

  private Predicate<AuthSession> makeAnnoPredicate(CheckPermission permissionAnno, String resourceName) {
    var rules = Arrays.asList(permissionAnno.value()).stream().filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    if (rules.isEmpty()) {
      throw new IllegalArgumentException("@CheckPermission.value must have at least one rule");
    }
    var predicates = rules.stream().map(t -> makePermissionPredicate(t, resourceName)).collect(Collectors.toList());

    if (predicates.size() == 1) {
      return new PermissionAnnoPredicate(predicates.get(0), permissionAnno.subjectType());
    }
    if (permissionAnno.logic().equals(Logic.ALL)) {
      return new PermissionAnnoPredicate(Predicates.and(predicates), permissionAnno.subjectType());
    } else {
      return new PermissionAnnoPredicate(Predicates.or(predicates), permissionAnno.subjectType());
    }

  }

  /**
   * 根据权限规则生成权限校验器
   * @param rule 权限规则, @{@link CheckPermission#value()}
   * @param resourceName
   * @return
   */
  public static Predicate<Collection<Permission>> makePermissionPredicate(String permission, String resourceName) {
    var containsAny = StringUtils.contains(permission, '|');
    var containsAll = StringUtils.contains(permission, '&');
    if (containsAny && containsAll) {
      throw new IllegalArgumentException("CheckPermission can't contain both '|' and '&'");
    }
    if (containsAny) {
      var permissions = StringUtils.split(permission, '|');
      return Predicates.or(Arrays.stream(permissions).filter(StringUtils::isNotBlank)
          .map(t -> makeSinglePredicate(t, resourceName)).collect(Collectors.toList()));

    }
    if (containsAll) {
      var permissions = StringUtils.split(permission, '&');
      return Predicates.and(Arrays.stream(permissions).filter(StringUtils::isNotBlank)
          .map(t -> makeSinglePredicate(t, resourceName)).collect(Collectors.toList()));
    }

    return makeSinglePredicate(permission, resourceName);

  }

  private static Predicate<Collection<Permission>> makeSinglePredicate(String permission, String resourceName) {
    permission = StringUtils.trim(permission);
    if (StringUtils.startsWith(permission, ":")) {
      if (StringUtils.isBlank(resourceName)) {
        throw new IllegalArgumentException(
            "@CheckPermission(" + permission + ") can't start with ':' without @CheckResource specified.");
      }
      permission = resourceName + permission;
    }
    return new PermissionPredicate(permission);
  }

  public static class PermissionPredicate implements Predicate<Collection<Permission>> {

    private final WildcardPermission permission;

    public PermissionPredicate(String permission) {
      this.permission = WildcardPermission.of(permission);
    }

    @Override
    public boolean test(Collection<Permission> permissions) {
      if (CollectionUtils.isEmpty(permissions)) {
        return false;
      }
      return permissions.stream()
          .anyMatch(t -> t.implies(this.permission));
    }
  }

  private static class PermissionAnnoPredicate implements Predicate<AuthSession> {

    private final Predicate<Collection<Permission>> permission;
    private final Set<String> userTypes;

    public PermissionAnnoPredicate(Predicate<Collection<Permission>> permission, String[] userTypes) {
      this.permission = permission;
      this.userTypes = new HashSet<>(Arrays.asList(userTypes));
    }

    @Override
    public boolean test(AuthSession session) {
      //check user type
      var userType = session.getSubjectType();
      if (!userTypes.isEmpty() && (Objects.isNull(userType) || !userTypes.contains(userType))) {
        return false;
      }
      return permission.test(session.getSubjectPermissions());
    }
  }

}
