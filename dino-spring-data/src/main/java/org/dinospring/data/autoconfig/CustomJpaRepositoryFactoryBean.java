// Copyright 2021 dinospring.cn
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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
    extends JpaRepositoryFactoryBean<T, S, ID> {
  @Autowired
  BeanFactory beanFactory;

  public CustomJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
    this.addRepositoryFactoryCustomizer(repositoryFactory -> {
      repositoryFactory.setBeanFactory(beanFactory);
      repositoryFactory.addRepositoryProxyPostProcessor(
          (factory, repositoryInformation) -> factory.addAdvice(new MethodInterceptor() {

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
              log.info("jpa method invoce: {}", invocation);
              repositoryInformation.isQueryMethod(invocation.getMethod());
              return invocation.proceed();
            }

          }));
    });
  }

}
