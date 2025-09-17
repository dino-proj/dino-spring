// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import cn.dinodev.spring.data.annotion.EnableDinoDataJdbc;

/**
 * DinoJdbc Repositories Registrar
 * @author Cody Lu
 * @date 2023-12-28 08:47:13
 */

@Component
public class DinoJdbcRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

  @Override
  @NonNull
  protected Class<? extends Annotation> getAnnotation() {
    return EnableDinoDataJdbc.class;
  }

  @Override
  @NonNull
  protected RepositoryConfigurationExtension getExtension() {
    return new DinoJdbcRepositoryConfigExtension();
  }

}
