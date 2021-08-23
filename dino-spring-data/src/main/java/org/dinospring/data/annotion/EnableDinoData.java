package org.dinospring.data.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.data.autoconfig.CustomJpaRepositoryFactoryBean;
import org.dinospring.data.dao.impl.JdbcSelectExecutorImpl;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableJpaRepositories(basePackages = "org.dinospring", repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class, repositoryBaseClass = JdbcSelectExecutorImpl.class)
public @interface EnableDinoData {
  /**
   * Base packages to scan for annotated components. please include "org.dinospring"
   */
  @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackages")
  String[] basePackages() default { "org.dinospring" };
}
