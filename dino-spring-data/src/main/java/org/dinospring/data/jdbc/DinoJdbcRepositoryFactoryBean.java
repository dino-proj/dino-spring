// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.jdbc;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.repository.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
public class DinoJdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends JdbcRepositoryFactoryBean<T, S, ID> {
  final BeanFactory beanFactory;

  public DinoJdbcRepositoryFactoryBean(Class<? extends T> repositoryInterface, BeanFactory beanFactory) {
    super(repositoryInterface);
    this.beanFactory = beanFactory;
    this.addRepositoryFactoryCustomizer(repositoryFactory -> {
      repositoryFactory.setBeanFactory(beanFactory);
      repositoryFactory.addRepositoryProxyPostProcessor(
          (factory, repositoryInformation) -> factory.addAdvice((MethodInterceptor) invocation -> {
            log.debug("repository method invoc: {}", invocation);
            repositoryInformation.isQueryMethod(invocation.getMethod());
            return invocation.proceed();
          }));
    });
  }

}
