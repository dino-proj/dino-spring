// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.auth.Permission;
import org.dinospring.auth.annotation.CheckIgnore;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.auth.annotation.CheckResource;
import org.dinospring.auth.annotation.Logic;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.commons.function.Predicates;
import org.springframework.core.annotation.AnnotatedElementUtils;

import lombok.Data;

/**
 * 用户权限校验器
 * @author Cody Lu
 * @date 2022-04-09 15:27:04
 */

public class AuthzCheckerPermission extends AbstractAuthzChecker<CheckPermission, List<Predicate<AuthSession>>> {

  public AuthzCheckerPermission() {
    super(CheckPermission.class, CheckIgnore.Type.PERMISSION);
  }

  @Override
  protected List<Predicate<AuthSession>> getMethodInvocationMeta(MethodInvocation mi,
      Collection<CheckPermission> annosInClass, Collection<CheckPermission> annosInMethod) {
    var resourceAnno = AnnotatedElementUtils.getMergedAnnotation(mi.getThis().getClass(), CheckResource.class);

    var resourceConfig = ResourceConfig.of(resourceAnno);

    return Stream.concat(annosInClass.stream(), annosInMethod.stream())
        .map(t -> this.makeAnnoPredicate(t, resourceConfig)).collect(
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

  private Predicate<AuthSession> makeAnnoPredicate(CheckPermission permissionAnno, ResourceConfig resourceConfig) {
    var rules = Arrays.asList(permissionAnno.value()).stream().filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    if (rules.isEmpty()) {
      throw new IllegalArgumentException("@CheckPermission.value must have at least one rule");
    }
    var predicates = rules.stream().map(t -> makePermissionPredicate(t, resourceConfig.getResourceName()))
        .collect(Collectors.toList());

    var subjectTypes = permissionAnno.subjectType().length > 0 ? permissionAnno.subjectType()
        : resourceConfig.getSubjectTypes();
    var exclueSubjectTypes = permissionAnno.exclueSubjectTypes().length > 0 ? permissionAnno.exclueSubjectTypes()
        : resourceConfig.getExclueSubjectTypes();
    var exclueRoles = permissionAnno.exclueRoles().length > 0 ? permissionAnno.exclueRoles()
        : resourceConfig.getExclueRoles();
    if (predicates.size() == 1) {
      return new PermissionAnnoPredicate(predicates.get(0), subjectTypes, exclueSubjectTypes, exclueRoles);
    }
    if (Logic.ALL.equals(permissionAnno.logic())) {
      return new PermissionAnnoPredicate(Predicates.and(predicates), subjectTypes, exclueSubjectTypes, exclueRoles);
    } else {
      return new PermissionAnnoPredicate(Predicates.or(predicates), subjectTypes, exclueSubjectTypes, exclueRoles);
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
    private final Set<String> subjectTypes;
    private final Set<String> exclueSubjectTypes;
    private final Set<String> exclueRoles;

    public PermissionAnnoPredicate(Predicate<Collection<Permission>> permission, String[] subjectTypes,
        String[] exclueSubjectTypes, String[] exclueRoles) {
      this.permission = permission;
      this.subjectTypes = new HashSet<>(Arrays.asList(subjectTypes));
      this.exclueSubjectTypes = new HashSet<>(Arrays.asList(exclueSubjectTypes));
      this.exclueRoles = new HashSet<>(Arrays.asList(exclueRoles));
    }

    @Override
    public boolean test(AuthSession session) {
      // check if user type is exclued, return true
      var userType = session.getSubjectType();
      if (this.exclueSubjectTypes.contains(userType)) {
        return true;
      }

      // check if user role is exclued, return true
      var userRoles = session.getSubjectRoles();
      if (CollectionUtils.isNotEmpty(userRoles)) {
        if (this.exclueRoles.stream().anyMatch(userRoles::contains)) {
          return true;
        }
      }

      // check user type
      if (Objects.isNull(userType) || !this.subjectTypes.contains(userType)) {
        return false;
      }

      // test permission
      return this.permission.test(session.getSubjectPermissions());
    }
  }

  @Data
  private static class ResourceConfig {
    private String resourceName;
    private String[] exclueRoles;
    private String[] exclueSubjectTypes;
    private String[] subjectTypes;

    public static ResourceConfig of(CheckResource resourceAnno) {
      var config = new ResourceConfig();
      if (Objects.nonNull(resourceAnno)) {
        config.setResourceName(resourceAnno.name());
        config.setExclueRoles(resourceAnno.exclueRoles());
        config.setExclueSubjectTypes(resourceAnno.exclueSubjectTypes());
        config.setSubjectTypes(resourceAnno.subjectType());
      }
      return config;
    }
  }

}
