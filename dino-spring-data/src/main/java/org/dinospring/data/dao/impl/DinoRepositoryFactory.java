package org.dinospring.data.dao.impl;

import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 *
 * @author tuuboo
 * @date 2022-03-31 22:05:23
 */

public class DinoRepositoryFactory extends RepositoryFactorySupport {

  @Override
  public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object getTargetRepository(RepositoryInformation metadata) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
    // TODO Auto-generated method stub
    return null;
  }

}
