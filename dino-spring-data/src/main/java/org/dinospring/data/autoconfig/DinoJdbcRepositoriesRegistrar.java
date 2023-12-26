package org.dinospring.data.autoconfig;

import java.lang.annotation.Annotation;

import org.dinospring.data.annotion.EnableDinoDataJdbc;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.stereotype.Component;

@Component
public class DinoJdbcRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

  @Override
  protected Class<? extends Annotation> getAnnotation() {
    return EnableDinoDataJdbc.class;
  }

  @Override
  protected RepositoryConfigurationExtension getExtension() {
    return new DinoJdbcRepositoryConfigExtension();
  }

}
