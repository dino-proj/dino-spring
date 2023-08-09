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

package org.dinospring.core.service;

import org.dinospring.commons.bean.BeanSafeCache;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.utils.TypeUtils;

import jakarta.annotation.Nonnull;

/**
 *
 * @author Cody LU
 * @date 2022-05-31 19:38:06
 */

public interface ServiceBeanResolver<S extends ServiceBase<?, ?>> {

  static final BeanSafeCache<Service<?, ?>> SERVICE_MAPPING_CACHE = new BeanSafeCache<>();

  /**
  * Service 服务实例
  * @return
  */
  @Nonnull
  default S service() {
    var service = SERVICE_MAPPING_CACHE.getOrElse(this.getClass(),
        cls -> ContextHelper.findBean(TypeUtils.getGenericParamClass(cls, ServiceBeanResolver.class, 0)));
    return TypeUtils.cast(service);
  }
}
