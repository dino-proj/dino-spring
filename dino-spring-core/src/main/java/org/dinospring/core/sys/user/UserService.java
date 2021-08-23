package org.dinospring.core.sys.user;

import java.io.Serializable;
import java.util.Optional;

import org.dinospring.commons.sys.User;

public interface UserService<T extends User<K>, K extends Serializable> {

  /**
   * 根据用户类型和用户ID获取用户信息
   * @param userType 用户类型
   * @param userId 用户ID
   * @return
   */
  Optional<T> getUserById(String userType, String userId);

  void onLogin(String userType, String userId);

}
