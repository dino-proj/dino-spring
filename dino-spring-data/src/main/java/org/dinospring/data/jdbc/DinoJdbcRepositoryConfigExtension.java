// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.jdbc;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jdbc.repository.config.JdbcRepositoryConfigExtension;

/**
 * DinoJdbc Repository Config Extension
 * support {@link jakarta.persistence.Table} and {@link org.springframework.data.relational.core.mapping.Table}
 * @author Cody Lu
 * @date 2023-12-28 08:48:07
 */

public class DinoJdbcRepositoryConfigExtension extends JdbcRepositoryConfigExtension {

  @Override
  public String getModuleName() {
    return "DinoJdbc";
  }

  @Override
  protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
    return List.of(jakarta.persistence.Table.class, org.springframework.data.relational.core.mapping.Table.class);
  }

}