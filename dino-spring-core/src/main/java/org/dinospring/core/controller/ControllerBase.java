// Copyright 2021 dinodev.cn
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

package org.dinospring.core.controller;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.service.ServiceBase;
import org.dinospring.core.service.ServiceBeanResolver;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;

/**
 *
 * @author tuuboo
 */

public interface ControllerBase<S extends ServiceBase<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, K extends Serializable>
    extends ServiceBeanResolver<S> {

  /**
   * Vo类的Class
   * @return
   */
  @Nonnull
  default Class<VO> voClass() {
    return TypeUtils.getGenericParamClass(this, CrudControllerBase.class, 2);
  }

  /**
   * Entity类的Class
   * @return
   */
  @Nonnull
  default Class<E> entityClass() {
    return TypeUtils.getGenericParamClass(this, CrudControllerBase.class, 1);
  }

}
