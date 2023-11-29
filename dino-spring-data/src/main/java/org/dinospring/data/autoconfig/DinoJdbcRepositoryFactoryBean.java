// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.data.autoconfig;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.repository.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
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
