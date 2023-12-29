// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.jdbc;

import java.lang.annotation.Annotation;

import org.dinospring.data.annotion.EnableDinoDataJdbc;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.stereotype.Component;

/**
 * DinoJdbc Repositories Registrar
 * @author Cody Lu
 * @date 2023-12-28 08:47:13
 */

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
