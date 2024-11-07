// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.dao.processor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

/**
 *
 * @author Cody Lu
 */

public class TenantJpaRepositoryPostProcessor implements RepositoryProxyPostProcessor {

  @Override
  public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
    factory.addAdvice(new MethodInterceptor() {

      @Override
      public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
      }

    });

  }

}
