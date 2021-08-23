package org.dinospring.core.sys.org;

import java.io.Serializable;
import java.util.Optional;

import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface UserRepositoryBase<T extends UserEntityBase<K>, K extends Serializable>
    extends CURDRepositoryBase<T, K> {

  @Query("FROM #{#entityName} e WHERE e.mobile=:mobile and e.tenantId=:tenantId")
  Optional<T> findUserByMobile(String tenantId, String mobile);

  @Query("FROM #{#entityName} e WHERE e.loginName=:username and e.tenantId=:tenantId")
  Optional<T> findUserByLoginName(String tenantId, String username);

  @Transactional(rollbackFor = Exception.class)
  @Modifying
  @Query("UPDATE #{#entityName} e set lastLoginAt = now() WHERE e.id=:id and e.tenantId=:tenantId")
  void updateLastLogin(String tenantId, K id);
}
