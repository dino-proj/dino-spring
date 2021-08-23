package org.dinospring.core.sys.org;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.dinospring.commons.sys.UserType;

public enum DefaultUserType implements UserType {
  SYS("sys"), ADMIN("admin"), CLIENT("client"), API("api"), GUEST("guest");

  private String userType;

  private DefaultUserType(String userType) {
    this.userType = userType;
  }

  @Override
  public String getType() {
    return userType;
  }

  @Override
  public String toString() {
    return this.getType();
  }

  @Override
  public List<UserType> allTypes() {
    return Arrays.stream(DefaultUserType.values()).collect(Collectors.toList());
  }

  public static UserType of(String userType) {
    return DefaultUserType.valueOf(userType.toUpperCase());
  }
}
