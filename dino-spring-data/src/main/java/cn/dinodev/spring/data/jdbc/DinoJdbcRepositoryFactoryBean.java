// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc;

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
public final class DinoJdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends JdbcRepositoryFactoryBean<T, S, ID> {
  final BeanFactory beanFactory;

  /**
   * 私有构造函数，防止直接实例化
   * @param repositoryInterface Repository接口类
   * @param beanFactory Bean工厂
   */
  private DinoJdbcRepositoryFactoryBean(Class<? extends T> repositoryInterface, BeanFactory beanFactory) {
    super(repositoryInterface);
    this.beanFactory = beanFactory;
  }

  /**
   * 创建DinoJdbcRepositoryFactoryBean实例并配置Repository工厂定制器
   * @param <T> Repository类型
   * @param <S> 实体类型
   * @param <ID> ID类型
   * @param repositoryInterface Repository接口类
   * @param beanFactory Bean工厂
   * @return 配置好的DinoJdbcRepositoryFactoryBean实例
   */
  public static <T extends Repository<S, ID>, S, ID extends Serializable> DinoJdbcRepositoryFactoryBean<T, S, ID> create(
      Class<? extends T> repositoryInterface, BeanFactory beanFactory) {
    DinoJdbcRepositoryFactoryBean<T, S, ID> factoryBean = new DinoJdbcRepositoryFactoryBean<>(repositoryInterface,
        beanFactory);
    factoryBean.configureRepositoryFactory();
    return factoryBean;
  }

  /**
   * 配置Repository工厂定制器
   */
  private void configureRepositoryFactory() {
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
