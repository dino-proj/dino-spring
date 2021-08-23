package org.dinospring.core.sys.user;

import java.io.Serializable;

import org.dinospring.commons.sys.User;
import org.dinospring.commons.sys.UserType;

public interface UserServiceProvider {

  <T extends User<K>, K extends Serializable> UserService<T, K> resolveUserService(String userType);

  UserType resolveUserType(String userType);

}
