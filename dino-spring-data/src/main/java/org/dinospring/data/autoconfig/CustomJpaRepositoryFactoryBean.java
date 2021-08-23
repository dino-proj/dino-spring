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
