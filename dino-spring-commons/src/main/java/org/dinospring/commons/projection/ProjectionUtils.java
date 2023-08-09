// Copyright 2022 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
