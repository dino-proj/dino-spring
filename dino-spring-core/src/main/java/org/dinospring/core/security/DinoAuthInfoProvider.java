// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.dinospring.auth.Permission;
import org.dinospring.auth.session.AuthInfoProvider;
import org.dinospring.auth.support.AllPermission;
import org.dinospring.auth.support.WildcardPermission;
import org.dinospring.commons.sys.User;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Cody Lu
 * @date 2022-04-17 20:11:02
 */
public class DinoAuthInfoProvider implements AuthInfoProvider<User<?>> {

  @Autowired
  private UserServiceProvider userServiceProvider;

  @Override
  public Collection<Permission> getPermissions(User<?> subject) {
    var userService = userServiceProvider.resolveUserService(subject.getUserType());
    // 超级用户，拥有所有权限
    if (userService.isSuperAdmin(subject.getUserType(), subject.getId().toString())) {
      return Collections.singletonList(AllPermission.of());
    }
    var perms = userService.getPermissions(subject.getUserType(), subject.getId().toString());
    if (Objects.isNull(perms)) {
      return List.of();
    }
    return perms.stream().map(WildcardPermission::of).collect(Collectors.toList());
  }

  @Override
  public Collection<String> getRoles(User<?> subject) {
    var userService = userServiceProvider.resolveUserService(subject.getUserType());
    return List.copyOf(userService.getRoles(subject.getUserType(), subject.getId().toString()));
  }

}