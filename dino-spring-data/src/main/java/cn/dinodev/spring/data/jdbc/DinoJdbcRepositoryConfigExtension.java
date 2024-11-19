// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jdbc.repository.config.JdbcRepositoryConfigExtension;
import org.springframework.lang.NonNull;

/**
 * DinoJdbc Repository Config Extension
 * support {@link jakarta.persistence.Table} and {@link org.springframework.data.relational.core.mapping.Table}
 * @author Cody Lu
 * @date 2023-12-28 08:48:07
 */

public class DinoJdbcRepositoryConfigExtension extends JdbcRepositoryConfigExtension {

  @Override
  @NonNull
  public String getModuleName() {
    return "DinoJdbc";
  }

  @Override
  @NonNull
  protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
    return List.of(jakarta.persistence.Table.class, org.springframework.data.relational.core.mapping.Table.class);
  }

}