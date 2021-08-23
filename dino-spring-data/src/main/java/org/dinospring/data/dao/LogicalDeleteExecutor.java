package org.dinospring.data.dao;

import java.io.Serializable;

import org.dinospring.data.domain.EntityBase;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@Cacheable
@NoRepositoryBean
public interface LogicalDeleteExecutor<T extends EntityBase<K>, K extends Serializable> {
  @Modifying
  @Query("update #{#entityName} set deleted=true where id=:id")
  @CacheEvict(cacheNames = "repo", key = "#{#entityName}'.id:'+#key")
  void logicalDeleteById(@Param("id") K id);
}
