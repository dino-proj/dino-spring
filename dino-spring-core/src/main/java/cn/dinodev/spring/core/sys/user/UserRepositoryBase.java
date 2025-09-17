// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.user;

import java.io.Serializable;
import java.util.Optional;

import cn.dinodev.spring.commons.sys.UserType;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cody Lu
 */

@NoRepositoryBean
public interface UserRepositoryBase<T extends UserEntityBase<K>, K extends Serializable>
    extends CrudRepositoryBase<T, K> {

  /**
   * 根据手机号查询用户
   * @param tenantId
   * @param mobile
   * @param userType
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.mobile=:mobile and e.tenantId=:tenantId and e.userType=:userType")
  Optional<T> findUserByMobile(String tenantId, String mobile, UserType userType);

  /**
   * 根据手机号查询用户
   * @param tenantId
   * @param mobile
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.mobile=:mobile and e.tenantId=:tenantId")
  Optional<T> findUserByMobile(String tenantId, String mobile);

  /**
   * 根据登录用户名查询用户
   * @param tenantId
   * @param username
   * @param userType
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.loginName=:username and e.tenantId=:tenantId and e.userType=:userType")
  Optional<T> findUserByLoginName(String tenantId, String username, UserType userType);

  /**
   * 根据登录用户名查询用户
   * @param tenantId
   * @param username
   * @return
   */
  @Query("FROM #{#entityName} e WHERE e.loginName=:username and e.tenantId=:tenantId")
  Optional<T> findUserByLoginName(String tenantId, String username);

  /**
   * 更新用户最后登录时间
   * @param tenantId
   * @param id
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  @Query("UPDATE #{#entityName} e set lastLoginAt = now() WHERE e.id=:id and e.tenantId=:tenantId")
  void updateLastLogin(String tenantId, K id);
}
