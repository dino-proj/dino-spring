// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.projection;

import java.util.function.Supplier;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.function.Suppliers;
import org.springframework.data.projection.ProjectionFactory;

import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody LU
 * @date 2022-06-03 22:39:47
 */

@UtilityClass
public class ProjectionUtils {
  private static final Supplier<ProjectionFactory> PROJECTION_FACTORY_SUPPLIER = Suppliers.lazy(() -> {
    return ContextHelper.findBean(ProjectionFactory.class);
  });

  /**
   * 获取 ProjectionFactory bean 实例
   * @return
   */
  public static ProjectionFactory resolveProjectionFactory() {
    return PROJECTION_FACTORY_SUPPLIER.get();
  }
}
